package com.dwolla.testutils

import cats.effect._
import cats.effect.laws.util.Pledge

import scala.language.higherKinds

trait ConcurrentHelpers { self: IOSpec ⇒

  def completeThePledgeOnCancel[F[_]] = new PartiallyAppliedCompleteThePledgeOnCancel[F]

  class PartiallyAppliedCompleteThePledgeOnCancel[F[_]] {
    def apply[A](pledge: Pledge[A], a: A)(implicit F: Concurrent[F]): F[Fiber[F, Unit]] =
      F.start(F.cancelable[Unit](_ ⇒ pledge.complete[IO](a)))
  }

}
