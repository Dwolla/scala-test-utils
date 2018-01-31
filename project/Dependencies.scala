import sbt._

object Dependencies {
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.7"
  val specs2Core = "org.specs2" %% "specs2-core" % "3.8.6"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Core.revision
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "[2.4,)"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaActor.revision
  val specs2Matchers = "org.specs2" %% "specs2-matcher-extra" % specs2Core.revision
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
}
