package com.dwolla.testutils

import cats.effect._
import cats.implicits._
import cats.effect.concurrent.Deferred

import scala.language.higherKinds

trait ConcurrentHelpers { self: IOSpec =>

  def completeTheDeferredOnCancel[F[_]] = new PartiallyAppliedCompleteTheDeferredOnCancel[F]

  class PartiallyAppliedCompleteTheDeferredOnCancel[F[_]] {
    def apply[A](deferred: Deferred[F, A], a: A)(implicit F: Concurrent[F], C: ContextShift[F]): F[Fiber[F, Unit]] =
      F.start(F.cancelable[Unit](_ => deferred.complete(a))) <* C.shift
  }

}
