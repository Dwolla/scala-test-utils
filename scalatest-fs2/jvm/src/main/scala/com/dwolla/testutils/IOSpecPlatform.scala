package com.dwolla.testutils

import scala.concurrent.ExecutionContext

trait IOSpecPlatform { self: IOSpec =>
  override implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}
