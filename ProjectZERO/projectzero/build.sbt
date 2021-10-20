import Dependencies._

ThisBuild / scalaVersion     := "2.13.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "projectzero",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.8.0",
    libraryDependencies += "net.liftweb" %% "lift-json" % "3.5.0"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
