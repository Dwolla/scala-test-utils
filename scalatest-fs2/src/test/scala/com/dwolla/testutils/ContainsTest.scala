package com.dwolla.testutils

import com.dwolla.testutils.scalatest._
import fs2._
import org.scalatest._

class ContainsTest extends FlatSpec with Matchers {

  "Contains" should "support fs2.Chunk" in {
    Chunk("hello", "goodbye") should contain("hello")
  }

  it should "support negation with fs2.Chunk too" in {
    Chunk("hello", "world") should not(contain("goodbye"))
  }

  "containsOneOf" should "support fs2.Chunk" in {
    Chunk(2, 3, 4) should contain oneOf(1, 2)
  }

  "containsNoneOf" should "support fs2.Chunk" in {
    Chunk(0, 1, 2) should contain noneOf(3, 4, 5)
  }

}
