package com.dwolla.testutils

import java.util.UUID

import cats.effect._
import cats.effect.laws.util.Pledge
import org.scalatest.Matchers

class ConcurrentHelpersTest extends IOSpec with Matchers with ConcurrentHelpers {

  "completeThePledgeOnCancel" should "complete the pledge on cancel" inIO {
    for {
      randomValue ← IO(UUID.randomUUID().toString)
      pledge ← Pledge[IO, String]
      fiber ← completeThePledgeOnCancel[IO](pledge, randomValue)
      _ ← fiber.cancel
      output ← pledge.await[IO]
    } yield {
      output should be(randomValue)
    }
  }

}
