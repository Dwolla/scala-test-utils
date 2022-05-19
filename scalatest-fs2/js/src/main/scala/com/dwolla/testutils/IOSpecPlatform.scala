package com.dwolla.testutils

import org.scalajs.macrotaskexecutor.MacrotaskExecutor

import scala.concurrent.ExecutionContext

trait IOSpecPlatform { self: IOSpec =>
  override implicit def executionContext: ExecutionContext = MacrotaskExecutor
}
