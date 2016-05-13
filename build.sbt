import java.lang.System.getenv

lazy val buildVersion = {
  val mainVersion = "1.0"
  val minorVersion = Option(getenv("TRAVIS_BUILD_NUMBER"))
  minorVersion match {
    case Some(v: String) ⇒ s"$mainVersion.$v"
    case None ⇒ mainVersion + "-SNAPSHOT"
  }
}

lazy val buildSettings = Seq(
  name := "TestUtils",
  version := buildVersion,
  organization := "com.dwolla",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-feature"),
  homepage := Some(url("https://github.com/Dwolla/scala-test-utils")),
  description := "Test utilities for Scala projects",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  startYear := Option(2015),

  libraryDependencies ++= {
    val specs2Version = "[3.3,)"
    Seq(
      "ch.qos.logback"              %  "logback-classic"            % "[1.1.3,)",
      "org.specs2"                  %% "specs2-core"                % specs2Version,
      "org.specs2"                  %% "specs2-mock"                % specs2Version         % Test,
      "org.specs2"                  %% "specs2-matcher-extra"       % specs2Version         % Test,
      "com.typesafe.scala-logging"  %% "scala-logging"              % "3.4.0"               % Test
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
