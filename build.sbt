import Dependencies._

lazy val baseName = "TestUtils"

lazy val SCALA_2_12 = "2.12.15"
lazy val SCALA_2_13 = "2.13.6"

inThisBuild(List(
  scalaVersion := SCALA_2_13,
  crossScalaVersions := Seq(SCALA_2_13, SCALA_2_12),
  organization := "com.dwolla",
  description := "Test utilities for Scala projects",
  homepage := Option(url("https://github.com/Dwolla/scala-test-utils")),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  startYear := Option(2015),
  developers := List(
    Developer(
      "bpholt",
      "Brian Holt",
      "bholt@dwolla.com",
      url("https://dwolla.com")
    )
  ),
  githubWorkflowTargetTags ++= Seq("v*"),
  githubWorkflowPublishTargetBranches :=
    Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
  githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"))),
  githubWorkflowPublish := Seq(
    WorkflowStep.Sbt(
      List("ci-release"),
      env = Map(
        "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
        "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
        "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
        "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
      )
    )
  ),
))

lazy val core = (project in file("core"))
  .settings(
    name := baseName,
    libraryDependencies ++= Seq(
      logback,
      specs2Core % Test
    )
  )

lazy val scalaTestFs2 = (sbtcrossproject.CrossPlugin.autoImport.crossProject(JVMPlatform, JSPlatform) crossType sbtcrossproject.CrossType.Pure in file("scalatest-fs2"))
  .settings(
    name := s"$baseName-scalatest-fs2",
    libraryDependencies ++= Seq(
      scalaTestFlatSpec.value,
      scalaTestShouldMatchers.value,
      scalaCheck.value,
      fs2Core.value,
      catsEffect.value,
      catsEffectLaws.value,
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  )

lazy val scalaTestFs2JVM = scalaTestFs2.jvm
lazy val scalaTestFs2JS = scalaTestFs2.js

lazy val specs2Akka = (project in file("specs2-akka"))
  .settings(
    name := s"$baseName-specs2-akka",
    libraryDependencies ++= Seq(
      akkaActor,
      akkaTestKit,
    ),
  )
  .dependsOn(specs2)

lazy val specs2 = (project in file("specs2"))
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
  .settings(skip in publish := true)
  .aggregate(core, specs2, specs2Akka, scalaTestFs2JVM, scalaTestFs2JS)
