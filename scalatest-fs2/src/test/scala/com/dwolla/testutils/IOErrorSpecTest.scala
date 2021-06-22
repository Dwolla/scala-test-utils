package com.dwolla.testutils

import cats.effect.IO
import org.scalatest.EitherValues
import org.scalatest.exceptions.TestFailedException
import org.scalatest.matchers.should.Matchers

class IOErrorSpecTest extends IOSpec with Matchers with EitherValues with IOErrorSpec {
  behavior of "IOErrorSpec.shouldThrowA"

  it should "pass successfully when an error of the correct type is thrown" inIO {
    shouldThrowA[CatchMe] {
      IO.raiseError(new CatchMe)
    }
  }

  it should "fail when an error of a different type is thrown" inIO {
    for {
      result <- shouldThrowA[CatchMe] {
                  IO.raiseError(new Exception)
                }.attempt
    } yield result.left.value shouldBe a[TestFailedException]
  }

  it should "fail when no error is thrown" inIO {
    for {
      result <- shouldThrowA[CatchMe] {
                  IO.pure(1)
                }.attempt
    } yield result.left.value shouldBe a[TestFailedException]
  }

  private class CatchMe extends Exception
}
