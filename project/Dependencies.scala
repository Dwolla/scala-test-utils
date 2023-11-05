import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  private val scalaTestV = "3.2.16"

  val SjsMacroTaskExecutor = Def.setting("org.scala-js" %%% "scala-js-macrotask-executor" % "1.0.0")
  val SjsSecureRandom = Def.setting("org.scala-js" %%% "scalajs-java-securerandom" % "1.0.0")
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.12"
  val specs2Core = "org.specs2" %% "specs2-core" % "4.20.3"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Core.revision
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.6.19"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaActor.revision
  val specs2Matchers = "org.specs2" %% "specs2-matcher-extra" % specs2Core.revision
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
  val scalaTestFlatSpec = Def.setting("org.scalatest" %%% "scalatest-flatspec" % scalaTestV)
  val scalaTestShouldMatchers = Def.setting("org.scalatest" %%% "scalatest-shouldmatchers" % scalaTestV)
  val scalaCheck = Def.setting("org.scalacheck" %%% "scalacheck" % "1.17.0")
  val fs2Core = Def.setting("co.fs2" %%% "fs2-core" % "2.5.11")

  private val catsEffectVersion = "2.5.5"
  val catsEffect = Def.setting("org.typelevel" %%% "cats-effect" % catsEffectVersion)
  val catsEffectLaws = Def.setting("org.typelevel" %%% "cats-effect-laws" % catsEffectVersion)
}
