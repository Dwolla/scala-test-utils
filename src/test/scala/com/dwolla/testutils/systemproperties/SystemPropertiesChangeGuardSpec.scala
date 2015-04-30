package com.dwolla.testutils.systemproperties

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

class SystemPropertiesChangeGuardSpec extends Specification {

  sequential
  "SystemPropertiesCacher" should {

    "store off properties specified" >> {
      val key = "test.prop"
      val originalValue = "original value"
      System.setProperty(key, originalValue)

      SystemPropertiesChangeGuard.saveExisting(key) (setPropertyAndAssertItWasSet(key))

      System.getProperty(key) must_== originalValue
    }

    "unset values that didn't exist before" >> {
      val key = "test.prop"
      System.clearProperty(key)

      SystemPropertiesChangeGuard.saveExisting(key) (setPropertyAndAssertItWasSet(key))

      System.getProperty(key) must beNull
    }
  }

  private def setPropertyAndAssertItWasSet(key: String): MatchResult[Any] = {
    val newValue = "new value"
    System.setProperty(key, newValue)

    System.getProperty(key) must_== newValue
  }
}
