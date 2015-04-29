import java.lang.System.getenv

val mainVersion = "0.1"
val minorVersion = Option(getenv("BUILD_NUMBER"))

val buildVersion = minorVersion match {
  case Some(v: String) => s"$mainVersion.$v"
  case None => s"$mainVersion-SNAPSHOT"
}

resolvers += "artifactory" at "http://artifactory:8081/artifactory/repo"

lazy val buildSettings = Seq(
  name := "TestUtils",
  version := buildVersion,
  organization := "com.dwolla",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-feature")
)

val specs2Version = "3.5"

libraryDependencies ++= Seq(
  "ch.qos.logback"              %  "logback-classic"            % "1.1.3",
  "org.specs2"                  %% "specs2-core"                % specs2Version,
  "org.specs2"                  %% "specs2-mock"                % specs2Version        % "test",
  "org.specs2"                  %% "specs2-matcher-extra"       % specs2Version        % "test",
  "org.mockito"                 %  "mockito-all"                % "1.9.5"              % "test",
  "com.typesafe.scala-logging"  %% "scala-logging"              % "3.1.0"              % "test"
)

lazy val pipeline = TaskKey[Unit]("pipeline", "Runs the full build pipeline: compile, test, integration tests")
pipeline <<= (test in IntegrationTest).dependsOn(test in Test)

val app = (project in file("."))
  .settings(buildSettings: _*)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
