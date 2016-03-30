import Dependencies._
import Global._

lazy val commonSettings = Seq(
  organization := "org.dcs",
  version := dcsVersion,
  scalaVersion := scVersion,
  checksums in update := Nil,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked"),
  javacOptions in doc := Seq("-source", "1.8"),
  resolvers ++= Seq(
    localMavenRepository
  )
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "org.dcs.parent"
  ).
  aggregate(core)

lazy val coreName = "org.dcs.core"
lazy val core = (project in file(coreName)).
  enablePlugins(SbtOsgi).
  configs(IntegrationTest).
  settings(commonSettings: _*).
  settings(Defaults.itSettings: _*).
  settings(
    name := coreName,
    OsgiKeys.bundleSymbolicName := coreName,
    OsgiKeys.exportPackage := Seq(name.value + ".*"),
    OsgiKeys.importPackage := Seq("*"),
    OsgiKeys.requireCapability := paxCdiCapabilities,
    moduleName := name.value,
    libraryDependencies ++= coreDependencies
  ).
  settings(osgiSettings: _*)
