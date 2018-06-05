package com.dwolla.testutils.scalatest

import cats._
import cats.implicits._
import fs2._
import org.scalatest.enablers.Containing

trait ScalaTestCatsInstances {
  implicit def ContainingContravariant: Contravariant[Containing] = new Contravariant[Containing] {
    override def contramap[A, B](fa: Containing[A])(f: B ⇒ A): Containing[B] = new Containing[B] {
      override def contains(container: B, element: Any): Boolean = fa.contains(f(container), element)

      override def containsOneOf(container: B, elements: Seq[Any]): Boolean = fa.containsOneOf(f(container), elements)

      override def containsNoneOf(container: B, elements: Seq[Any]): Boolean = fa.containsNoneOf(f(container), elements)
    }
  }

  implicit def fs2ChunkContains[T](implicit listContains: Containing[List[T]]): Containing[Chunk[T]] = listContains.contramap(_.toList)
}
