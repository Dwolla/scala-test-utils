package com.dwolla.testutils.javaio

import java.io.ByteArrayInputStream

import org.specs2.mutable.Specification

class CloseTrackingInputStreamSpec extends Specification with IsClosedMatchers {
  "CloseTrackingInputStream" should {

    "keep track of whether the proxied inputstream has been closed" >> {
      val stream = new CloseTrackingInputStream(new ByteArrayInputStream(Array()))

      stream must not(beClosed)

      stream.close()

      stream must beClosed
    }
  }
}
