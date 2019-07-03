package com.dwolla.testutils

import cats.effect._
import org.scalatest.Matchers

class StateHelpersTest extends IOSpec with StateHelpers with Matchers {
  val someTuple = IO(("state", Option("output")))
  val noneTuple: IO[(String, Option[String])] = IO(("state", None))

  "filterForSomeValue" should "unwrap a Some value returned by the State monad" inIO {
    for {
      (state, output) <- filterForSomeValue(someTuple)
    } yield {
      state should be("state")
      output should be("output")
    }
  }

  it should "fail if output returned by State monad is None" inIO {
    filterForSomeValue(noneTuple).attempt.map {
      case Left(ex) => ex.getMessage should be("The state output tuple was (_, None) but (_, Some(_)) was required")
      case Right(_) => fail("the attempt should have failed")
    }
  }

  "filterForNoValue" should "accept a None value returned by the State monad" inIO {

    for {
      state <- filterForNoValue(noneTuple)
    } yield {
      state should be("state")
    }
  }

  it should "fail if output returned by State monad is Some(_)" inIO {
    filterForNoValue(someTuple).attempt.map {
      case Left(ex) => ex.getMessage should be("The state output tuple was (_, Some(_)) but (_, None) was required")
      case Right(_) => fail("the attempt should have failed")
    }
  }

}
