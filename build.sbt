import sbt.Keys._

lazy val root = (project in file(".")).aggregate(backend, api, utils, model).settings(commonSettings:_*).settings(name := "rest-service")

lazy val backend = project in file("backend") settings(commonSettings:_*) settings(
  name := "backend",
  libraryDependencies := Seq(
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % sprayVersion,
    "io.spray" %% "spray-caching" % sprayVersion,
    "io.spray" %% "spray-testkit" % sprayVersion % "test" excludeAll ExclusionRule(organization = "org.specs2"),
    "com.typesafe.scala-logging" %% "scala-logging" % loggingVersion,
    "com.typesafe" % "config" % configVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )
) dependsOn api

lazy val api = project in file("api") settings(commonSettings:_*) settings(name := "api") dependsOn (utils, model)

lazy val utils = project in file("utils") settings(commonSettings:_*) settings(
  name := "utils",
  libraryDependencies := Seq(
    "org.scodec" %% "scodec-bits" % scodecVersion,
    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )) dependsOn model

lazy val model = project in file("model") settings(commonSettings:_*) settings(
  name := "model",
  libraryDependencies := Seq(
    "org.scalaz" %% "scalaz-core" % scalazVersion
  ))

lazy val commonSettings = Seq(
  organization := "com.github.feodorov",
  version := "1.0",
  scalaVersion := "2.11.8"
)

lazy val sprayVersion = "1.3.2"
lazy val scodecVersion = "1.1.0"
lazy val scalazVersion = "7.2.2"
lazy val scalaTestVersion = "2.2.6"
lazy val loggingVersion = "3.4.0"
lazy val configVersion = "1.2.1"
lazy val akkaVersion = "2.4.5"
lazy val logbackVersion = "1.1.7"

mainClass in (Compile, run) := Some("com.github.feodorov.backend.Main")
run in Compile <<= (run in Compile in backend)

resolvers += "spray repo" at "http://repo.spray.io"