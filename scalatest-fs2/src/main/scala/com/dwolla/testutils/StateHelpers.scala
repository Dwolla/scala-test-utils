package com.dwolla.testutils

import cats._
import cats.implicits._
import org.scalactic.source

import scala.language.higherKinds

trait StateHelpers { self: IOSpec ⇒

  def filterForSomeValue[F[_] : Functor, S, A](tuple: F[(S, Option[A])])(implicit pos: source.Position): F[(S, A)] = tuple.map {
    case (s, Some(a)) ⇒ (s, a)
    case _ ⇒ fail("The state output tuple was (_, None) but (_, Some(_)) was required")
  }

  def filterForNoValue[F[_] : Functor, S, A](tuple: F[(S, Option[A])])(implicit pos: source.Position): F[S] = tuple.map {
    case (x, None) ⇒ x
    case _ ⇒ fail("The state output tuple was (_, Some(_)) but (_, None) was required")
  }

}
