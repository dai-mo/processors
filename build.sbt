import Common._
import Dependencies._
import com.typesafe.sbt.GitPlugin.autoImport._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease._

val projectName = "org.dcs.parent"

lazy val core =
  OsgiProject("core", "org.dcs.core").
    settings(libraryDependencies ++= coreDependencies)

val defaultDatabaseLib = "slick-postgres"
// The target database can be provided when building the 'repo' project
// by using the system property option '-Ddatabase=<target>'.
// Possible targets include postgres, cassandra
lazy val databaseLib = sys.props.getOrElse("databaseLib", default = defaultDatabaseLib)

lazy val dataProjectName = "org.dcs.data"
lazy val dataProjectID   = "data"

lazy val data =
  OsgiProject(dataProjectID, dataProjectName).
    settings(
      name := dataProjectName,
      moduleName := dataProjectName + "." + databaseLib,
      libraryDependencies ++= dataDependencies(databaseLib),
      unmanagedSourceDirectories in Compile ++= sourceDirs(baseDirectory.value, databaseLib),
      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        artifact.name + "." + databaseLib + "-" + module.revision + classifier(artifact.classifier) + "." + artifact.extension
      },
      slickPostgres := slickPostgresCodeGenTask.value
    )


def classifier(cl: Option[String]): String = if(cl.isDefined) "-" + cl.get else  ""

lazy val osgi = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "org.dcs.parent"
  ).
  aggregate(core, data)


lazy val slick = config("slick") describedAs "Sbt configuration for slick commands"
lazy val slickPostgres = TaskKey[Seq[File]]("gen-postgres").in(slick)

lazy val slickPostgresCodeGenTask = (baseDirectory, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "src" / "slick" / "scala" / "org" / "dcs" / "data" / "postgres").getPath
  val username = sys.props.get("dbUser")
  val password = sys.props.get("dbPassword")
  if(username.isEmpty || password.isEmpty) throw new IllegalArgumentException("One of the system properties dbUser or dbPassword is not set")
  val host = sys.props.getOrElse("dbHost", "dcs-postgres")
  val port = sys.props.getOrElse("dbPort", "5432")
  val database = sys.props.getOrElse("dbName", "dcs")

  val url = "jdbc:postgresql://" + host + ":" + port + "/" + database
  val jdbcDriver = "org.postgresql.Driver"
  val slickDriver = "slick.driver.PostgresDriver"
  val pkg = "org.dcs.data.postgres"
  toError(r.run("slick.codegen.SourceCodeGenerator",
    cp.files,
    Array(slickDriver, jdbcDriver, url, outputDir, pkg, username.get, password.get),
    s.log))
  val fname = outputDir +  "/Tables.scala"
  Seq(file(fname))
}





// ------- Versioning , Release Section --------

// Build Info
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := projectName

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

