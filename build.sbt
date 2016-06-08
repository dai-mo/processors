import Dependencies._
import Common._

lazy val osgi = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "org.dcs.parent"
  ).
  aggregate(core)

lazy val core =
  OsgiProject("core", "org.dcs.core").
  settings(libraryDependencies ++= coreDependencies)


lazy val data = (
  OsgiProject("data", "org.dcs.data").
  settings(libraryDependencies ++= dataDependencies)
)
