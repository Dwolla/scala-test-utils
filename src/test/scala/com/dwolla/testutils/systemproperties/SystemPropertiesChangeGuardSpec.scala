package com.dwolla.testutils.systemproperties

import com.dwolla.testutils.systemproperties.SystemPropertiesChangeGuard._
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

class SystemPropertiesChangeGuardSpec extends Specification {

  sequential
  "SystemPropertiesCacher" should {
    "store off properties specified" >> {
      val key = "test.prop"
      val originalValue = "original value"
      System.setProperty(key, originalValue)

      saveExisting(key) (setPropertyAndAssertItWasSet(key))

      System.getProperty(key) must_== originalValue
    }

    "unset values that didn't exist before" >> {
      val key = "test.prop"
      System.clearProperty(key)

      saveExisting(key) (setPropertyAndAssertItWasSet(key))

      System.getProperty(key) must beNull
    }

    "allow key->value pairs to be cached and then set in one step" >> {
      val key = "test.prop"
      val originalValue = "original value"
      val newValue = "new value"
      System.setProperty(key, originalValue)

      withSystemProperties(key -> newValue) {
        System.getProperty(key) must_== newValue
      }

      System.getProperty(key) must_== originalValue
    }

    "allow key->value pairs to be cached and then set in one step" >> {
      val key = "test.prop"
      val originalValue = "original value"
      System.setProperty(key, originalValue)

      withSystemProperties(key -> None) {
        System.getProperty(key) must beNull
      }

      System.getProperty(key) must_== originalValue
    }

    "allow multiple key->value pairs to be specified" >> {
      val key1 = "key1"
      val key2 = "key2"

      val original1 = "original1"
      val original2 = "original2"

      val newValue = "new value"

      System.setProperty(key1, original1)
      System.setProperty(key2, original2)

      withSystemProperties(key1 -> None, key2 -> newValue) {
        System.getProperty(key1) must beNull
        System.getProperty(key2) must_== newValue
      }

      System.getProperty(key1) must_== original1
      System.getProperty(key2) must_== original2
    }

  }

  "implicit string2OptionString converter" should {
    "convert Strings to Some(String)" >> {
      string2OptionString("test") must_== Some("test")
    }

    "recognize null input and use none instead" >> {
      string2OptionString(null) must_== None
    }
  }

  private def setPropertyAndAssertItWasSet(key: String): MatchResult[Any] = {
    val newValue = "new value"
    System.setProperty(key, newValue)

    System.getProperty(key) must_== newValue
  }
}
