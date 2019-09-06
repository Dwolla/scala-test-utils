import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val specs2Core = "org.specs2" %% "specs2-core" % "4.5.1"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Core.revision
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "[2.4,)"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaActor.revision
  val specs2Matchers = "org.specs2" %% "specs2-matcher-extra" % specs2Core.revision
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % "3.0.8")
  val fs2Core = Def.setting("co.fs2" %%% "fs2-core" % "1.1.0-M1")

  private val catsEffectVersion = "2.0.0-RC2"
  val catsEffect = Def.setting("org.typelevel" %%% "cats-effect" % catsEffectVersion)
  val catsEffectLaws = Def.setting("org.typelevel" %%% "cats-effect-laws" % catsEffectVersion)
}
