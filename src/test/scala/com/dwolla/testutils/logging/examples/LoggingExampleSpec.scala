package com.dwolla.testutils.logging.examples

import ch.qos.logback.classic.Level.INFO
import com.dwolla.testutils.logging.LoggingTest
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mock.Mockito
import org.specs2.specification.Scope

class LoggingExampleSpec extends LoggingTest with Mockito {

  private trait Setup extends Scope {
    // only necessary if running tests sequentially
    resetCapturedLogs()
  }

  "code under test" should {
    "log “Hello, World!” at the info level" in new Setup {
      CodeUnderTest.greet("World")

      "Hello, World!" must haveBeenLoggedAtLevel(INFO)
    }
  }
}

private object CodeUnderTest extends LazyLogging {
  def greet(entity: String): Unit = logger.info("Hello, {}!", entity)
}
