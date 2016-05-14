package com.dwolla.testutils.sequences

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class ContainSliceMatcherSpec extends Specification with ContainSliceMatcher {
  "ContainSliceMatcher" should {
    "match when the given sequence contains the expected sequence" in {

      Seq(1, 2, 3, 4) must containSlice(2, 3)
    }

    "not match when the given sequence does not contain the expected sequence" in {
      Seq(1, 2, 3, 4) must not(containSlice(2, 4))
    }

    "gracefully handle empty lists" in {
      Seq.empty[Int] must not(containSlice(1))
    }
  }
}

class StartWithMatcherSpec extends Specification with StartWithMatcher {
  "StartWithMatcher" should {
    "match when the given sequence starts with the expected value" in {
      Seq(1, 2, 3) must startWith(1)
    }

    "not match when the given sequence does not start with the expected value" in {
      Seq(2, 3) must not(startWith(1))
    }

    "gracefully handle empty lists" in {
      Seq.empty[Int] must not(startWith(1))
    }
  }
}

class EndWithMatcherSpec extends Specification with EndWithMatcher {
  "EndWithMatcher" should {
    "match when the given sequence ends with the expected value" in {
      Seq(1, 2, 3) must endWith(3)
    }

    "not match when the given sequence does not end with the expected value" in {
      Seq(1, 2, 3) must not(endWith(2))
    }

    "gracefully handle empty lists" in {
      Seq.empty[Int] must not(endWith(1))
    }
  }
}

class AdditionalSeqMatchersSpec extends Specification {
  trait Setup extends Scope {
    val testClass = new AdditionalSeqMatchers {}
  }

  "AdditionalSeqMatchers" should {
    "implement ContainSliceMatcher" in new Setup {
      testClass must beAnInstanceOf[ContainSliceMatcher]
    }

    "implement ContainSliceMatcher" in new Setup {
      testClass must beAnInstanceOf[StartWithMatcher]
    }

    "implement ContainSliceMatcher" in new Setup {
      testClass must beAnInstanceOf[EndWithMatcher]
    }
  }
}