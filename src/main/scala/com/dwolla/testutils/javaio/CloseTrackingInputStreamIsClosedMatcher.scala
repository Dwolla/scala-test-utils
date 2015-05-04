package com.dwolla.testutils.javaio

import org.specs2.matcher.{Expectable, Matcher}

trait IsClosedMatchers {
  def beClosed = new CloseTrackingInputStreamIsClosedMatcher
}

class CloseTrackingInputStreamIsClosedMatcher extends Matcher[CloseTrackingInputStream] {
  def apply[S <: CloseTrackingInputStream](v: Expectable[S]) = {
    result(v.value.isClosed, v.description + " is closed", v.description + " is open", v)
  }
}
