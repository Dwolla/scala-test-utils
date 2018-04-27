package com.dwolla.testutils.random

import java.time.Instant
import java.util.UUID

import scala.util.Random

trait Fakes {
  private final val Tlds = Seq("com", "org", "net", "edu", "gov", "io", "cool")

  protected final val rnd = new Random()

  def uuid(): UUID = UUID.randomUUID()

  def string(length: Int = 10): String = Random.alphanumeric.take(length).mkString

  def url(): String = s"http${if (bool()) "s"}://${string()}.${tld()}"

  def email(): String = s"${string(5)}@${string(5)}.${tld()}"

  def instant(): Instant = Instant.now()

  def bool(): Boolean = rnd.nextBoolean()

  def double(): Double = rnd.nextDouble()

  def float(): Double = rnd.nextFloat()

  def int(n: Int): Int = rnd.nextInt(n)

  def int(): Int = rnd.nextInt()

  def long(): Long = rnd.nextLong()

  def short(): Short = int(Short.MaxValue + 1).asInstanceOf[Short]

  def bigDecimal(): BigDecimal = BigDecimal(double())

  private def tld() = Tlds(int(Tlds.length))
}
