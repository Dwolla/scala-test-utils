package com.dwolla.testutils.logging

import ch.qos.logback.classic.{Level, Logger}
import org.slf4j.helpers.SubstituteLogger
import org.slf4j.{Logger => SLF4JLogger, LoggerFactory}
import org.specs2.matcher.{Expectable, Matcher}
import org.specs2.mutable.SpecificationLike
import org.specs2.text.Sentences._

trait LoggingTest extends SpecificationLike {
  protected val logAppenderName = "inMemory"
  private val appender = findInMemoryAppender(logAppenderName)

  def haveBeenLoggedAtLevel(logLevel: Level): Matcher[String] = new Matcher[String] {
    def apply[S <: String](s: Expectable[S]) = {
      val containsAtLevel = appender.containsAtLevel(s.value, logLevel)
      val errorMessage =
        if (containsAtLevel)
          "The expected logging occurred"
        else if (appender.contains(s.value))
          s"“${s.value}” was logged at the wrong level"
        else
          s"“${s.value}” was never logged"
      result(containsAtLevel, negateSentence(errorMessage), errorMessage, s)
    }
  }

  def resetCapturedLogs(): Unit = appender.reset()
  def dumpCapturedLogsToSysOut(): Unit = appender.dumpLogs()

  private def findInMemoryAppender(s: String): InMemoryAppender = {
    LoggerFactory.getLogger(SLF4JLogger.ROOT_LOGGER_NAME) match {
      case logger: Logger => logger.getAppender(s) match {
        case inMemoryAppender: InMemoryAppender => inMemoryAppender
        case _ => throw new IllegalStateException(s"Is the InMemoryAppender registered with logback in its configuration file with the name $s?")
      }
      case sub: SubstituteLogger => throw new IllegalStateException("SLF4J is probably still initializing. Is LoggingTest part of the outermost class wrapping your tests?")
      case _ => throw new IllegalStateException("Are you using LogBack logging?")
    }
  }
}
