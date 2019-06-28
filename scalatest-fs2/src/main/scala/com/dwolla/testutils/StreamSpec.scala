package com.dwolla.testutils

import cats.effect._
import org.scalactic.source
import org.scalatest.{Matchers, compatible}
import org.scalatest.flatspec.AsyncFlatSpecLike

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{higherKinds, implicitConversions, reflectiveCalls}

trait StreamSpec extends IOSpec with Matchers {
  import fs2._

  implicit def toInStream[B <: {def in(fun : ⇒ Future[compatible.Assertion])(implicit pos: source.Position): Unit}](any: B)(implicit pos: source.Position) = new InStream(any)(pos)

  implicit def toInStream[A, B <: {def in(fun : ⇒ Future[compatible.Assertion])(implicit pos: source.Position): Unit}](any: A)(implicit aToB: A ⇒ B, pos: source.Position) = new InStream(aToB(any))(pos)

  class InStream[B <: {def in(fun : ⇒ Future[compatible.Assertion])(implicit pos: source.Position): Unit}](val testDescription: B)(implicit pos: source.Position) {
    def inStream[F[_]](testStream: ⇒ Stream[F, compatible.Assertion])(implicit F: PureOrIO[F]): Unit = {
      new InIO(testDescription) inIO {
        F.ioStream(testStream)
          .compile
          .last.map(_.getOrElse(fail("Stream must not be empty")(pos)))
      }
    }
  }
}

trait IOSpec extends AsyncFlatSpecLike {

  override implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val timer: Timer[IO] = IO.timer(executionContext)
  implicit val cs: ContextShift[IO] = IO.contextShift(executionContext)

  implicit def toInIO[B <: {def in(fun : ⇒ Future[compatible.Assertion])(implicit pos: source.Position): Unit}](any: B) = new InIO(any)

  implicit def toInIO[A, B <: {def in(fun : ⇒ Future[compatible.Assertion])(implicit pos: source.Position): Unit}](any: A)(implicit aToB: A ⇒ B) = new InIO(aToB(any))

  class InIO[B <: {def in(fun : ⇒ Future[compatible.Assertion])(implicit pos: source.Position): Unit}](val testDescription: B) {
    def inIO(testEffect: ⇒ IO[compatible.Assertion]): Unit = {
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
