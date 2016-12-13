import sbt.Keys._

name := "play-java-intro"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "uk.co.panaxiom" %% "play-jongo" % "2.0.0-jongo1.3",
  "com.google.code.gson" % "gson" % "2.7",
  "org.json" % "json" % "20090211",
  "javax.mail" % "mail" % "1.4.7",
  "commons-validator" % "commons-validator" % "1.4.0",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  "io.javaslang" % "javaslang" % "2.0.0",
  "org.apache.commons" % "commons-lang3" % "3.5",
  "com.nappin" %% "play-recaptcha" % "2.1"
)

