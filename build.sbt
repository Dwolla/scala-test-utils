lazy val buildSettings = Seq(
  name := "TestUtils",
  organization := "com.dwolla",
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.12.4", "2.11.11"),
  scalacOptions ++= Seq("-feature", "-deprecation"),
  homepage := Some(url("https://github.com/Dwolla/scala-test-utils")),
  description := "Test utilities for Scala projects",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  releaseVersionBump := sbtrelease.Version.Bump.Minor,
  releaseProcess := {
    import ReleaseTransformations._
    Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  },
  startYear := Option(2015),

  libraryDependencies ++= {
    import Dependencies._
    Seq(
      logback,
      specs2Core,
      specs2Mock % Provided,
      akkaActor % Compile,
      akkaTestKit % Compile,
      specs2Matchers % Test,
      scalaLogging % Test,
    )
  }
)

lazy val bintraySettings = Seq(
  bintrayVcsUrl := Some("https://github.com/Dwolla/scala-test-utils"),
  publishMavenStyle := false,
  bintrayRepository := "maven",
  bintrayOrganization := Option("dwolla"),
  pomIncludeRepository := { _ ⇒ false }
)

val app = (project in file("."))
  .settings(buildSettings ++ bintraySettings: _*)
