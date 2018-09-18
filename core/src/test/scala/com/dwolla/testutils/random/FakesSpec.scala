package com.dwolla.testutils.random

import java.net.URL

import org.specs2.mutable.Specification

import scala.util.Try

class FakesSpec extends Specification with Fakes {

  "url" should {
    "generate a parseable URL" in {
      val value = url()
      val result = Try(new URL(value))
      result.isSuccess must beTrue
    }

    "generate a parseable ssl URL" in {
      val value = url(true)
      value must startingWith("https://")
      Try(new URL(value)).isSuccess must beTrue
    }

    "generate a parseable standard URL" in {
      val value = url(false)
      value must startingWith("http://")
      Try(new URL(value)).isSuccess must beTrue
    }
  }
}
