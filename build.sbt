import Dependencies._
import Global._

lazy val commonSettings = Seq(
  organization := "org.dcs",
  version := dcsVersion,
  scalaVersion := scVersion,
  checksums in update := Nil,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked"),
  resolvers += localMavenRepository
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "org.dcs.parent"
  ).
  aggregate(core)

lazy val core = (project in file("org.dcs.core")).
  configs(IntegrationTest).
  settings(commonSettings: _*).
  settings(Defaults.itSettings: _*).
  settings(
    name := "org.dcs.core",
    libraryDependencies ++= coreDependencies
  )
