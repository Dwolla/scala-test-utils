package com.dwolla.testutils.concurrency

import com.dwolla.testutils.concurrency.PromiseMatchers.beCompleted
import com.dwolla.testutils.exceptions.NoStackTraceException
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.concurrent.Promise

class PromiseCompletedMatcherSpec extends Specification {
  trait Setup extends Scope {
    val promise = Promise[Unit]()
  }

  "PromiseCompletedMatcher" should {
    "match on a successful promise" in new Setup {
      promise.success(()) must beCompleted
    }

    "match on a failed promise" in new Setup {
      promise.failure(NoStackTraceException) must beCompleted
    }

    "not match on incomplete promise" in new Setup {
      promise must not(beCompleted)
    }
  }
}
