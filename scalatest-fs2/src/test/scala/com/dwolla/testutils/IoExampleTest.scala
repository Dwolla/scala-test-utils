package com.dwolla.testutils

class IoExampleTest extends IOSpec with org.scalatest.Matchers {

  "MyGreatCode" should "do something" inIO {
    for {
      x <- cats.effect.IO(1)
    } yield
      x should be(1)
  }
}
