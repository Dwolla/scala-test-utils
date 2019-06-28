package com.dwolla.testutils

import java.util.UUID

import cats.effect._
import cats.effect.concurrent.Deferred
import org.scalatest.Matchers

class ConcurrentHelpersTest extends IOSpec with Matchers with ConcurrentHelpers {

  "completeTheDeferredOnCancel" should "complete the Deferred on cancel" inIO {
    for {
      randomValue ← IO(UUID.randomUUID().toString)
      deferred ← Deferred[IO, String]
      fiber ← completeTheDeferredOnCancel[IO](deferred, randomValue)
      _ ← fiber.cancel
      output ← deferred.get
    } yield {
      output should be(randomValue)
    }
  }

}
