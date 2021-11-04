import Dependencies._

ThisBuild / scalaVersion     := "2.11.0"    // Need to be this one for Hive
ThisBuild / version          := "v1SNAP"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"


lazy val root = (project in file("."))
  .settings(
    name := "newp1",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.0"/*,
    libraryDependencies += "net.liftweb" %% "lift-json" % "2.6"
    libraryDependencies += "net.liftweb" %% "lift-webkit_2.11" % "3.1.0" */
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
