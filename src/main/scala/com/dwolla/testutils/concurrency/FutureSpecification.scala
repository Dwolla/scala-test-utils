package com.dwolla.testutils.concurrency

import org.specs2.mutable.SpecificationLike

import concurrent.{Await, Future}
import scala.concurrent.duration._

trait FutureSpecification extends SpecificationLike {
  implicit def seqToFuture[T](seq: Seq[T]): Future[Seq[T]] = Future.successful(seq)
  implicit def futureToSeq[T](future: Future[Seq[T]]): Seq[T] = Await.result(future, 2 seconds)
}
