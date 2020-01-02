package com.dwolla.testutils

class StreamExampleTest extends StreamSpec with org.scalatest.matchers.should.Matchers {

  "MyGreatCode" should "do something" inStream {
    for {
      x <- fs2.Stream.emits(Seq(1, 2, 3))
    } yield {
      x should (be <= 3  and be > 0)
    }
  }

}
