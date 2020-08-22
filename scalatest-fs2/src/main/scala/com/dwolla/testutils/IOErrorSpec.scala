package com.dwolla.testutils

import cats.effect.IO
import org.scalactic.source.Position
import org.scalatest.{Assertion, Assertions, Succeeded}

import scala.reflect.ClassTag

trait IOErrorSpec extends Assertions {

  def shouldThrowA[T <: AnyRef](
    anIO: IO[_]
  )(implicit classTag: ClassTag[T], pos: Position): IO[Assertion] =
    anIO.attempt
      .map {
        case Left(_: T)    => Succeeded
        case Left(error)   =>
          fail(
            s"Expected an exception of type ${classTag.runtimeClass.getName}, but $error was thrown"
          )(pos)
        case Right(result) =>
          fail(
            s"Expected an exception of type ${classTag.runtimeClass.getName}, but the IO succeeded with result $result"
          )(
            pos
          )
      }

  def shouldThrowAn[T <: AnyRef](
    anIO: IO[_]
  )(implicit classTag: ClassTag[T], pos: Position): IO[Assertion] =
    shouldThrowA[T](anIO)
}
