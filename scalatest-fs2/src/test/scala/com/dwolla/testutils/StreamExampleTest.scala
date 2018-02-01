package com.dwolla.testutils

class StreamExampleTest extends StreamSpec with org.scalatest.Matchers {

  "MyGreatCode" should "do something" inStream {
    fs2.Stream.emits(Seq(1, 2, 3))
      .map(_ should (be <= 3  and be > 0))
  }

}
