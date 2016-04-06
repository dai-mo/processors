import Dependencies._
import Global._
import Common._

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "org.dcs.parent"
  ).
  aggregate(core,data)

lazy val core = (
  OsgiProject("core", "org.dcs.core").
  dependsOn(data).
  settings(libraryDependencies ++= coreDependencies)
)

lazy val data = (
  OsgiProject("data", "org.dcs.data").
  settings(libraryDependencies ++= dataDependencies)
)
