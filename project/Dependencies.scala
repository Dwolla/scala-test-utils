import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  private val scalaTestV = "3.2.10"

  val logback = "ch.qos.logback" % "logback-classic" % "1.2.6"
  val specs2Core = "org.specs2" %% "specs2-core" % "4.7.0"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Core.revision
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.6.16"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaActor.revision
  val specs2Matchers = "org.specs2" %% "specs2-matcher-extra" % specs2Core.revision
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
  val scalaTestFlatSpec = Def.setting("org.scalatest" %%% "scalatest-flatspec" % scalaTestV)
  val scalaTestShouldMatchers = Def.setting("org.scalatest" %%% "scalatest-shouldmatchers" % scalaTestV)
  val scalaCheck = Def.setting("org.scalacheck" %%% "scalacheck" % "1.15.4")
  val fs2Core = Def.setting("co.fs2" %%% "fs2-core" % "2.5.9")

  private val catsEffectVersion = "2.2.0"
  val catsEffect = Def.setting("org.typelevel" %%% "cats-effect" % catsEffectVersion)
  val catsEffectLaws = Def.setting("org.typelevel" %%% "cats-effect-laws" % catsEffectVersion)
}
