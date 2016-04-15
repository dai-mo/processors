import sbt._

object Global {
  // Versions
  lazy val scVersion = "2.11.8"
  lazy val nanonetArtifactoryBaseUrl = "http://artifactory.openshift.nanonet"

  // Repositories
  val localMavenRepository = (
    "Local Maven Repository" at "file://" +
    Path.userHome.absolutePath +
    "/.m2/repository")

  val nanonetMavenRepository = (
        "Nanonet Maven Repository" at nanonetArtifactoryBaseUrl + "/artifactory/libs-snapshot-local/"
    )

  val paxCdiCapabilities = "org.ops4j.pax.cdi.extension;" +
    "filter:=\"(&(extension=pax-cdi-extension)(version>=0.12.0)(!(version>=1.0.0)))\"," +
    "osgi.extender;" +
    "filter:=\"(osgi.extender=pax.cdi)\"," +
    "org.ops4j.pax.cdi.extension;" +
    "filter:=\"(extension=pax-cdi-extension)\""

}