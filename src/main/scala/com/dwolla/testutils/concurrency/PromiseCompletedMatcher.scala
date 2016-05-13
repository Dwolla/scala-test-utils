package com.dwolla.testutils.concurrency

import org.specs2.matcher.{Expectable, MatchResult, Matcher}

import scala.concurrent.Promise

class PromiseCompletedMatcher extends Matcher[Promise[_]] {
  override def apply[S <: Promise[_]](t: Expectable[S]): MatchResult[S] = result(t.value.isCompleted, s"${t.description} has completed", s"${t.description} has not completed", t)
}

object PromiseMatchers {
  def beCompleted = new PromiseCompletedMatcher
}
