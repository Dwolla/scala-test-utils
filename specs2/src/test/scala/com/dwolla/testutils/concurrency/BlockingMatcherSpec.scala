package com.dwolla.testutils.concurrency

import com.dwolla.testutils.concurrency.BlockingMatcher.invokeBlockingFunction
import org.specs2.concurrent.{ExecutionEnv, NoImplicitExecutionContextFromExecutionEnv}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.concurrent.{ExecutionContext, Future, blocking}

class BlockingMatcherSpec(implicit ee: ExecutionEnv) extends Specification with NoImplicitExecutionContextFromExecutionEnv {

  trait Setup extends Scope

  "BlockingAssertions" should {
    "fail a test when the blocking keyword is called for but not used in the implementation" in new Setup {
      { implicit ec: ExecutionContext =>
        Future {
          Thread.sleep(100)
        }
      } must not(invokeBlockingFunction(ee))
    }

    "succeed when the blocking keyword is called for and used in the implementation" in new Setup {
      { implicit ec: ExecutionContext =>
        Future {
          blocking {
            Thread.sleep(100)
          }
        }
      } must invokeBlockingFunction(ee)
    }
  }
}
