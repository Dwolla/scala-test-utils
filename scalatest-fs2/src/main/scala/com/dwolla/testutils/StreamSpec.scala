package com.dwolla.testutils

import cats.effect._
import com.dwolla.testutils.IOSpec.InFutureTest
import org.scalactic.source
import org.scalatest.flatspec._
import org.scalatest.compatible
import org.scalatest.matchers.should.Matchers

import scala.concurrent._
import scala.language.reflectiveCalls
import cats.effect.Temporal

trait StreamSpec extends IOSpec with Matchers {
  import fs2._

  implicit def toInStream[B <: {def in(fun : => Future[compatible.Assertion])(implicit pos: source.Position): Unit}](any: B)(implicit pos: source.Position): InStream[B] = new InStream(any)(pos)

  implicit def toInStream[A, B <: {def in(fun : => Future[compatible.Assertion])(implicit pos: source.Position): Unit}](any: A)(implicit aToB: A => B, pos: source.Position): InStream[B] = new InStream(aToB(any))(pos)

  class InStream[B <: {def in(fun : => Future[compatible.Assertion])(implicit pos: source.Position): Unit}](val testDescription: B)(implicit pos: source.Position) {
    def inStream[F[_]](testStream: => Stream[F, compatible.Assertion])(implicit F: PureOrIO[F]): Unit = {
      new InIO(testDescription) inIO {
        F.ioStream(testStream)
          .compile
          .last.map(_.getOrElse(fail("Stream must not be empty")(pos)))
      }
    }
  }
}

object IOSpec {
  type InFutureTest = {def in(fun : => Future[compatible.Assertion])(implicit pos: source.Position): Unit}
}

trait IOSpec extends AsyncFlatSpecLike {

  override implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val timer: Temporal[IO] = IO.timer(executionContext)
  implicit val cs: ContextShift[IO] = IO.contextShift(executionContext)

  implicit def toInIO[B <: InFutureTest](any: B): InIO[B] = new InIO(any)

  implicit def toInIO[A, B <: InFutureTest](any: A)(implicit aToB: A => B): InIO[B] = new InIO(aToB(any))

  class InIO[B <: InFutureTest](val testDescription: B) {
    def inIO(testEffect: => IO[compatible.Assertion]): Unit = {
      testDescription in {
        testEffect.unsafeToFuture()
      }
    }
  }
}

/* // these tests are both expected to fail… not sure how to rewrite them as passing tests so they can be uncommented

class StreamSpecSpec extends StreamSpec with Matchers {

  behavior of "StreamSpec"

  it should "fail if a item midway through the stream fails" inStream {
    Stream(1, 2, 3)
      .map(_ should (be(1) or be(3)))
  }

  it should "fail if the stream is empty" inStream Stream.empty
}

 */
