

import sbt._
import Keys._
import com.typesafe.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi
import Dependencies._
import Global._

object Common {
  lazy val UNIT = config("unit") extend(Test)
  lazy val IT = config("it") extend(Test)

  lazy val commonSettings = Seq(
    organization := "org.dcs",
    scalaVersion := scVersion,
    // FIXME: Not really sure if adding the scala version
    //        in the published artifact name is a good idea
    crossPaths := false,
    // FIXME: Parallel execution of threads have been switched off
    //        because the arquillian cdi environment does not
    //        work correctly when running tests in parallel
    parallelExecution in Test := false,
    // FIXME: Checksum check when downloading artifacts is disabled
    //        because some projects do not update their
    //        checksums when deploying artifacts
    checksums in update := Nil,
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked"),
    javacOptions in doc := Seq("-source", "1.8"),
    fork in Test := true,
    javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    parallelExecution in Test := false

  )

  def BaseProject(projectID: String,
                  projectName: String) =
    Project(projectID, file(projectName)).
      settings(commonSettings: _*).
      settings(
        name := projectName,
        moduleName := projectName).
      configs(IT).
      settings(inConfig(IT)(Defaults.testTasks): _*).
      settings(testOptions in IT := Seq(Tests.Argument("-n", "IT"))).
      configs(UNIT).
      settings(inConfig(UNIT)(Defaults.testTasks): _*).
      settings(testOptions in UNIT := Seq(
        Tests.Argument("-l", "IT"),
        Tests.Argument("-l", "E2E"))
      )

  lazy val paxCdiCapabilities = "org.ops4j.pax.cdi.extension;" +
    "filter:=\"(&(extension=pax-cdi-extension)(version>=0.12.0)(!(version>=1.0.0)))\"," +
    "osgi.extender;" +
    "filter:=\"(osgi.extender=pax.cdi)\"," +
    "org.ops4j.pax.cdi.extension;" +
    "filter:=\"(extension=pax-cdi-extension)\""

  def OsgiProject(projectID: String, projectName: String, exportPackages: Seq[String] = Nil) =
    Project(projectID, file(projectName)).
      enablePlugins(SbtOsgi).
      configs(IntegrationTest).
      settings(commonSettings: _*).
      settings(Defaults.itSettings: _*).


      settings(
        name := projectName,
        OsgiKeys.bundleSymbolicName := projectName,
        OsgiKeys.exportPackage := Seq(name.value + ".*") ++ exportPackages ,
        OsgiKeys.importPackage := Seq("*"),
        OsgiKeys.requireCapability := paxCdiCapabilities,
        moduleName := name.value).
      settings(osgiSettings: _*)

}
