package com.dwolla.testutils.javaio

import java.io.{FilterInputStream, InputStream}

import scala.concurrent.Promise

class CloseTrackingInputStream(protected val inputStream: InputStream) extends FilterInputStream(inputStream) {
  private val promisedClosed = Promise[Unit]()

  def isClosed: Boolean = promisedClosed.isCompleted

  override def close(): Unit = {
    promisedClosed.trySuccess(())
    super.close()
  }
}
