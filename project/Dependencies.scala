import sbt._

object Dependencies {
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.7"
  val specs2Core = "org.specs2" %% "specs2-core" % "3.8.6"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Core.revision
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "[2.4,)"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaActor.revision
  val specs2Matchers = "org.specs2" %% "specs2-matcher-extra" % specs2Core.revision
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  val fs2Core = "co.fs2" %% "fs2-core" % "0.10.0"
  val fs2Io = "co.fs2" %% "fs2-io" % fs2Core.revision
  val catsEffect = "org.typelevel" %% "cats-effect" % "0.8"
}
