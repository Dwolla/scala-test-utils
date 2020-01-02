package com.dwolla.testutils.concurrency

import org.specs2.mock.Mockito
import org.specs2.mutable.{Specification, SpecificationLike}
import org.specs2.specification.Scope

import concurrent.Future

@deprecated("use Specs2 .await matchers instead", "1.1")
class FutureSpecificationSpec extends Specification with Mockito {

  //noinspection ScalaDeprecation
  trait Setup extends Scope {
    val classToTest = new FutureSpecification {}
  }

  "FutureSpecification" should {

    "extend SpecificationLike" in new Setup {
      classToTest must beAnInstanceOf[SpecificationLike]
    }

    "seqToFuture should return a Success wrapping the sequence" in new Setup {
      classToTest.seqToFuture(Seq(1, 2)).value.get.get must_== Seq(1, 2)
    }

    "futureToSeq should await the sequence in the future" in new Setup {
      classToTest.futureToSeq(Future.successful(Seq(3, 4))) must_== Seq(3, 4)
    }
  }

}
