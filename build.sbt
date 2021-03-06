import Common._
import Dependencies._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.sbt.GitPlugin.autoImport._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease._

val projectName = "org.dcs.parent"

val license = Some(HeaderLicense.Custom(
  """Copyright (c) 2017-2018 brewlabs SAS
    |
    |Licensed under the Apache License, Version 2.0 (the "License");
    |you may not use this file except in compliance with the License.
    |You may obtain a copy of the License at
    |
    |    http://www.apache.org/licenses/LICENSE-2.0
    |
    |Unless required by applicable law or agreed to in writing, software
    |distributed under the License is distributed on an "AS IS" BASIS,
    |WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    |See the License for the specific language governing permissions and
    |limitations under the License.
    |
    |""".stripMargin
))

lazy val core =
  OsgiProject("core", "org.dcs.core").
    enablePlugins(BuildInfoPlugin).
    settings(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
      buildInfoPackage := name.value,
      headerLicense := license
    ).
    settings(libraryDependencies ++= coreDependencies).
    dependsOn(data)


lazy val dataProjectName = "org.dcs.data"
lazy val dataProjectID   = "data"

lazy val slick = config("slick") describedAs "Sbt configuration for slick commands"
lazy val slickPostgres = TaskKey[Seq[File]]("codegen").in(slick)

lazy val data =
  OsgiProject(dataProjectID, dataProjectName, Seq("slick.jdbc.hikaricp")).
    settings(
      name := dataProjectName,
      moduleName := dataProjectName,
      libraryDependencies ++= dataDependencies,
      slickPostgres := {
        if(sys.props.get("config.file").isEmpty)
          throw new IllegalArgumentException("config.file vm argument is not set")
        val outputDir = (baseDirectory.value / "src" / "main" / "scala" ).getPath

        val conf: Config = ConfigFactory.load()
        val db: Config = conf.getConfig("postgres")

        val username = Option(db.getString("user"))
        val password = Option(db.getString("password"))
        if(username.isEmpty || password.isEmpty) throw
          new IllegalArgumentException("One of the system properties dbUser or dbPassword is not set")

        val url = db.getString("url")
        val jdbcDriver = "org.postgresql.Driver"
        val slickDriver = "slick.driver.PostgresDriver"
        val pkg = "org.dcs.data.slick"
        toError((runner in Compile).value.run("slick.codegen.SourceCodeGenerator",
          (dependencyClasspath in Compile).value.files,
          Array(slickDriver, jdbcDriver, url, outputDir, pkg, username.get, password.get),
          streams.value.log))

        val fname = outputDir +  "/Tables.scala"
        Seq(file(fname))
      }
    ).
    // FIXME: This should be removed once the
    //        slick-hikaricp osgi manifest issue,
    //        https://github.com/slick/slick/issues/1694
    //        is resolved
    settings(
    OsgiKeys.embeddedJars := (Keys.externalDependencyClasspath in Compile).value map (_.data) filter (a =>
      a.getName.startsWith("slick-hikaricp")),
    headerLicense := license
  )


def classifier(cl: Option[String]): String = if(cl.isDefined) "-" + cl.get else  ""

lazy val osgi = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "org.dcs.parent"
  ).
  settings(
  	headerLicense := license
  ).
  aggregate(data, core)



// ------- Versioning , Release Section --------

// Git
showCurrentGitBranch

git.useGitDescribe := true

git.baseVersion := "0.0.0"

val VersionRegex = "v([0-9]+.[0-9]+.[0-9]+)-?(.*)?".r

git.gitTagToVersionNumber := {
  case VersionRegex(v,"SNAPSHOT") => Some(s"$v-SNAPSHOT")
  case VersionRegex(v,"") => Some(v)
  case VersionRegex(v,s) => Some(s"$v-$s-SNAPSHOT")
  case v => None
}

lazy val bumpVersion = settingKey[String]("Version to bump - should be one of \"None\", \"Major\", \"Patch\"")
bumpVersion := "None"

releaseVersion := {
  ver => bumpVersion.value.toLowerCase match {
    case "none" => Version(ver).
      map(_.withoutQualifier.string).
      getOrElse(versionFormatError)
    case "major" => Version(ver).
      map(_.withoutQualifier).
      map(_.bump(sbtrelease.Version.Bump.Major).string).
      getOrElse(versionFormatError)
    case "patch" => Version(ver).
      map(_.withoutQualifier).
      map(_.bump(sbtrelease.Version.Bump.Bugfix).string).
      getOrElse(versionFormatError)
    case _ => sys.error("Unknown bump version - should be one of \"None\", \"Major\", \"Patch\"")
  }
}

releaseVersionBump := sbtrelease.Version.Bump.Minor
