package com.dwolla.testutils.systemproperties

import java.lang.System._

import org.specs2.specification.core.AsExecution

import scala.language.implicitConversions
import scala.sys.SystemProperties

object SystemPropertiesChangeGuard {
  def saveExisting[R: AsExecution](keys: String*)(functionToBeWrapped: => R): R = {
    val props = new SystemProperties
    val settings = keys.map(k => k -> props.get(k)).toMap

    try {
      functionToBeWrapped
    } finally {
      settings.foreach {
        case (k, None) => clearProperty(k)
        case (k, Some(v)) => setProperty(k, v)
      }
    }
  }

  def withSystemProperties[R: AsExecution](props: (String, Option[String])*)(functionToBeWrapped: => R): R = {
    saveExisting(props.map { case (key, _) => key }: _*) {
      props.foreach {
        case (key, maybeValue) => maybeValue match {
          case Some(value) => setProperty(key, value)
          case None => clearProperty(key)
        }
      }

      functionToBeWrapped
    }
  }

  implicit def string2OptionString(k: String): Option[String] = Option(k)
}
