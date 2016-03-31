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
  OsgiProject("org-dcs-core", "org.dcs.core").
  settings(libraryDependencies ++= coreDependencies)
)

lazy val data = (
  OsgiProject("org-dcs-data", "org.dcs.data").
  settings(libraryDependencies ++= dataDependencies)
)
