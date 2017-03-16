lazy val buildSettings = Seq(
  name := "TestUtils",
  version := "1.4.0",
  organization := "com.dwolla",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.11.8", "2.12.1"),
  scalacOptions ++= Seq("-feature", "-deprecation"),
  homepage := Some(url("https://github.com/Dwolla/scala-test-utils")),
  description := "Test utilities for Scala projects",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  startYear := Option(2015),

  libraryDependencies ++= {
    val specs2Version = "3.8.6"
    val akkaVersion = "[2.4,)"
    Seq(
      "ch.qos.logback"              %  "logback-classic"            % "1.1.7",
      "org.specs2"                  %% "specs2-core"                % specs2Version,
      "org.specs2"                  %% "specs2-mock"                % specs2Version         % Provided,
      "com.typesafe.akka"           %% "akka-actor"                 % akkaVersion           % Compile,
      "com.typesafe.akka"           %% "akka-testkit"               % akkaVersion           % Compile,
      "org.specs2"                  %% "specs2-matcher-extra"       % specs2Version         % Test,
      "com.typesafe.scala-logging"  %% "scala-logging"              % "3.5.0"               % Test
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
