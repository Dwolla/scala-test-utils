package com.dwolla.testutils

import cats.effect._
import cats.implicits._
import org.scalatest.matchers.should.Matchers

class NoConcurrentModificationIOSpec extends IOSpec with Matchers {

  behavior of "ScalaTest"

  it should "not run this in parallel with other tests" inIO {
    (0L to 10000000L).toList.reduce(_ |+| _).pure[IO].map(_ shouldBe a[Long])
  }

  it should "not run another test in parallel with other tests" inIO {
    (0L to 10000000L).toList.reduce(_ |+| _).pure[IO].map(_ shouldBe a[Long])
  }

}
