import java.lang.System.getenv

val mainVersion = "1.0"
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

val specs2Version = "[3.3,)"

libraryDependencies ++= Seq(
  "ch.qos.logback"              %  "logback-classic"            % "1.1.3",
  "org.specs2"                  %% "specs2-core"                % specs2Version,
  "org.specs2"                  %% "specs2-mock"                % specs2Version        % "test",
  "org.specs2"                  %% "specs2-matcher-extra"       % specs2Version        % "test",
  "com.typesafe.scala-logging"  %% "scala-logging"              % "3.1.0"              % "test"
)

val app = (project in file("."))
  .settings(buildSettings: _*)
  .settings(Defaults.itSettings: _*)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishArtifact in(Compile, packageBin) := true

publishArtifact in(Compile, packageDoc) := false

publishArtifact in(Compile, packageSrc) := true

publishArtifact in Test := false

publishTo := {
  val artifactory = "http://artifactory:8081/"
  if (buildVersion.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at artifactory + "artifactory/libs-snapshot-local")
  else
    Some("releases" at artifactory + "artifactory/libs-release-local")
}
