import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.7"
  val specs2Core = "org.specs2" %% "specs2-core" % "3.8.6"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Core.revision
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "[2.4,)"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaActor.revision
  val specs2Matchers = "org.specs2" %% "specs2-matcher-extra" % specs2Core.revision
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % "3.0.4")
  val fs2Core = Def.setting("co.fs2" %%% "fs2-core" % "0.10.5")

  private val catsEffectVersion = "0.10.1"
  val catsEffect = Def.setting("org.typelevel" %%% "cats-effect" % catsEffectVersion)
  val catsEffectLaws = Def.setting("org.typelevel" %%% "cats-effect-laws" % catsEffectVersion)
}
