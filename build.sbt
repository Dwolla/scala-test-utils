import Dependencies._

lazy val baseName = "TestUtils"

lazy val SCALA_2_12 = "2.12.17"
lazy val SCALA_2_13 = "2.13.11"

ThisBuild / scalaVersion := SCALA_2_13
ThisBuild / crossScalaVersions := Seq(SCALA_2_13, SCALA_2_12)
ThisBuild / organization := "com.dwolla"
ThisBuild / description := "Test utilities for Scala projects"
ThisBuild / homepage := Option(url("https://github.com/Dwolla/scala-test-utils"))
ThisBuild / licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
ThisBuild / startYear := Option(2015)
ThisBuild / developers := List(
    Developer(
      "bpholt",
      "Brian Holt",
      "bholt@dwolla.com",
      url("https://dwolla.com")
    )
  )
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("8"))
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
    Seq(RefPredicate.StartsWith(Ref.Tag("v")))
ThisBuild / githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release")))
ThisBuild / githubWorkflowPublish := Seq(
    WorkflowStep.Sbt(
      List("ci-release"),
      env = Map(
        "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
        "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
        "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
        "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
      )
    )
  )

lazy val core = (project in file("core"))
  .settings(
    name := baseName,
    libraryDependencies ++= Seq(
      logback,
      specs2Core % Test
    ),
    tpolecatScalacOptions ~= { _ -- Set(ScalacOptions.warnNonUnitStatement, ScalacOptions.warnUnusedNoWarn) },
  )

lazy val scalaTestFs2 = (crossProject(JVMPlatform, JSPlatform) in file("scalatest-fs2"))
  .settings(
    name := s"$baseName-scalatest-fs2",
    libraryDependencies ++= Seq(
      scalaTestFlatSpec.value,
      scalaTestShouldMatchers.value,
      scalaCheck.value,
      fs2Core.value,
      catsEffect.value,
      catsEffectLaws.value,
      SjsMacroTaskExecutor.value,
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    tpolecatScalacOptions ~= { _ -- Set(ScalacOptions.warnNonUnitStatement, ScalacOptions.warnUnusedNoWarn) },
  )
  .jsSettings(
    libraryDependencies += SjsSecureRandom.value,
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
    tpolecatScalacOptions ~= { _ -- Set(ScalacOptions.warnNonUnitStatement, ScalacOptions.warnUnusedNoWarn) },
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
    tpolecatScalacOptions ~= { _ -- Set(ScalacOptions.warnNonUnitStatement, ScalacOptions.warnUnusedNoWarn) },
  )
  .dependsOn(core)

lazy val scalaTestUtils = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(core, specs2, specs2Akka, scalaTestFs2JVM, scalaTestFs2JS)
