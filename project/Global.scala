import sbt._

object Global {

  val paxCdiCapabilities = "org.ops4j.pax.cdi.extension;" +
    "filter:=\"(&(extension=pax-cdi-extension)(version>=0.12.0)(!(version>=1.0.0)))\"," +
    "osgi.extender;" +
    "filter:=\"(osgi.extender=pax.cdi)\"," +
    "org.ops4j.pax.cdi.extension;" +
    "filter:=\"(extension=pax-cdi-extension)\""

}