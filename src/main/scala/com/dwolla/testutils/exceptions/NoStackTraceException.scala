package com.dwolla.testutils.exceptions

object NoStackTraceException extends RuntimeException("exception intentionally thrown by test", null, true, false)
