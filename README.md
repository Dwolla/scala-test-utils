# Scala Test Utilities

[![license](https://img.shields.io/github/license/Dwolla/scala-test-utils.svg?style=flat-square)]()

See the submodules’ READMEs for more information on what features are available.

|Submodule | Description | Artifact Name |
|----------|-------------|---------------|
|Core|Common utilities with minimal dependencies|`"com.dwolla" %% "testutils" % {version}`|
|[ScalaTest](http://www.scalatest.org) with [fs2](https://github.com/functional-streams-for-scala/fs2)|Async [FlatSpec-style](http://www.scalatest.org/user_guide/selecting_a_style) helpers for testing fs2 Streams or logic in the [cats-effect IO monad](https://github.com/typelevel/cats-effect)|`"com.dwolla" %% "testutils-scalatest-fs2" % {version}`|
|[Specs2](https://etorreborre.github.io/specs2/)|Various matchers and guards for testing with Specs2|`"com.dwolla" %% "testutils-specs2" % {version}`|
|Specs2 with [Akka](https://akka.io)|Specs2, with additional Akka helpers|`"com.dwolla" %% "testutils-specs2-akka" % {version}`|
