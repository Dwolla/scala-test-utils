package com.dwolla.testutils.concurrency

import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.{Expectable, MatchResult, Matcher}

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Try

class BlockingMatcher(timeout: Duration = 1.second)(implicit ee: ExecutionEnv) extends Matcher[ExecutionContext => Any] {
  override def apply[S <: (ExecutionContext) => Any](t: Expectable[S]): MatchResult[S] = {
    val blocked = Promise[Unit]()

    val oldContext = BlockContext.current
    val myContext = new BlockContext {
      override def blockOn[F](thunk: => F)(implicit permission: CanAwait): F = {
        blocked.success(())
        oldContext.blockOn(thunk)
      }
    }
    val ec = new ExecutionContext {
      override def execute(runnable: Runnable): Unit = {
        ee.ec execute new Runnable {
          override def run(): Unit = {
            BlockContext.withBlockContext(myContext) {
              runnable.run()
            }
          }
        }
      }

      override def reportFailure(t: Throwable): Unit = {
        ee.ec.reportFailure(t)
      }
    }

    t.value(ec)

    val completed = Try {
      Await.ready(blocked.future, timeout)
    }

    result(completed.isSuccess, "blocking was invoked", "blocking was not invoked", t)
  }
}

object BlockingMatcher {
  def invokeBlockingFunction(implicit ee: ExecutionEnv) = new BlockingMatcher
}
