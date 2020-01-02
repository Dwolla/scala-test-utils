package com.dwolla.testutils.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class InMemoryAppender extends AppenderBase[ILoggingEvent] {
  private val events: mutable.Map[Thread, ListBuffer[ILoggingEvent]] = new mutable.HashMap[Thread, ListBuffer[ILoggingEvent]]

  override def append(e: ILoggingEvent): Unit = {
    events.getOrElseUpdate(Thread.currentThread(), new ListBuffer[ILoggingEvent]).append(e)
    ()
  }

  def reset(): Unit = events.get(Thread.currentThread()).foreach(l => l.clear())

  def containsAtLevel(msg: String, logLevel: Level): Boolean =
    eventsForCurrentThread.filter(e => e.getLevel == logLevel)
      .map(e => e.getFormattedMessage).contains(msg)

  def contains(msg: String): Boolean =
    eventsForCurrentThread.map(e => e.getFormattedMessage).contains(msg)

  def dumpLogs(): Unit =
    eventsForCurrentThread.foreach(e => println(s"${Thread.currentThread().getName}: ${e.getFormattedMessage}"))

  private def eventsForCurrentThread = events.getOrElse(Thread.currentThread(), new ListBuffer[ILoggingEvent])
}
