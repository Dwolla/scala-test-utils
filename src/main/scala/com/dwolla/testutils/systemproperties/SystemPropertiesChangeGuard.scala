package com.dwolla.testutils.systemproperties

import java.lang.System._

import org.specs2.main.CommandLineAsResult

object SystemPropertiesChangeGuard {
  def saveExisting[R : CommandLineAsResult](keys: String*)(f: => R): R = {
    val props = new scala.sys.SystemProperties
    val settings: Map[String, Option[String]] =
      keys.map(k => k -> props.get(k)).toMap

    try {
      f
    } finally {
      settings.foreach {
        case (k, None) => clearProperty(k)
        case (k, Some(v)) => setProperty(k, v)
      }
    }
  }
}