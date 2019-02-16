import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "CoreIR_HLS",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0"
libraryDependencies += "io.argonaut" %% "argonaut" % "6.2.2"     
libraryDependencies += "io.argonaut" %% "argonaut-scalaz" % "6.2.2"  
libraryDependencies += "io.argonaut" %% "argonaut-monocle" % "6.2.2"  
libraryDependencies += "io.argonaut" %% "argonaut-cats" % "6.2.2"
