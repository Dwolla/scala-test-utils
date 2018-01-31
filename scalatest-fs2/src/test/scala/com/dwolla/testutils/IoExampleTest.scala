package com.dwolla.testutils

class IoExampleTest extends IOSpec with org.scalatest.Matchers {

  "MyGreatCode" should "do something" inIO {
    cats.effect.IO(1)
      .map(_ should be(1))
  }

}
