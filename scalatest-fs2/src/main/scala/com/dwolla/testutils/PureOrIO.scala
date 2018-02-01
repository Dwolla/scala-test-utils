package com.dwolla.testutils

import cats.effect.IO
import fs2._
import org.scalatest.compatible.Assertion

import scala.language.higherKinds

sealed trait PureOrIO[T[_]] {
  def ioStream(s: Stream[T, Assertion]): Stream[IO, Assertion]
}

object PureOrIO {

  implicit val pure = new PureOrIO[Pure] {
    override def ioStream(s: Stream[Pure, Assertion]): Stream[IO, Assertion] = s.covary[IO]
  }

  implicit val io = new PureOrIO[IO] {
    override def ioStream(s: Stream[IO, Assertion]): Stream[IO, Assertion] = s
  }

}
