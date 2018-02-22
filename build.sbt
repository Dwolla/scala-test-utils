import Dependencies._

lazy val baseName = "TestUtils"

lazy val commonSettings = Seq(
  organization := "com.dwolla",
  description := "Test utilities for Scala projects",
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.12.4", "2.11.11"),
  scalacOptions ++= Seq("-feature", "-deprecation"),
  homepage := Some(url("https://github.com/Dwolla/scala-test-utils")),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  releaseVersionBump := sbtrelease.Version.Bump.Minor,
  releaseCrossBuild := true,
  startYear := Option(2015),
  bintrayVcsUrl := Some("https://github.com/Dwolla/scala-test-utils"),
  bintrayRepository := "maven",
  bintrayOrganization := Option("dwolla"),
  pomIncludeRepository := { _ ⇒ false }
)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := baseName,
    libraryDependencies += logback,
  )

lazy val scalaTestFs2 = (sbtcrossproject.crossProject(JVMPlatform, JSPlatform) crossType sbtcrossproject.CrossType.Pure in file("scalatest-fs2"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-scalatest-fs2",
    libraryDependencies ++= Seq(
      scalaTest.value,
      fs2Core.value,
      catsEffect.value,
    ),
  )

lazy val scalaTestFs2JVM = scalaTestFs2.jvm
lazy val scalaTestFs2JS = scalaTestFs2.js

lazy val specs2Akka = (project in file("specs2-akka"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-specs2-akka",
    libraryDependencies ++= Seq(
      akkaActor,
      akkaTestKit,
    ),
  )
  .dependsOn(specs2)

lazy val specs2 = (project in file("specs2"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-specs2",
    libraryDependencies ++= Seq(
      specs2Core,
      specs2Mock % Provided,
      specs2Matchers % Test,
      scalaLogging % Test,
    ),
  )
  .dependsOn(core)

lazy val scalaTestUtils = (project in file("."))
  .settings(commonSettings: _*)
  .settings(noPublishSettings: _*)
  .aggregate(core, specs2, specs2Akka, scalaTestFs2JVM, scalaTestFs2JS)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false,
  Keys.`package` := file(""),
)
