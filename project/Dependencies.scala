import sbt._

object Dependencies {
  // Versions
  lazy val dcsVersion       = "1.0.0-SNAPSHOT"
  lazy val paxCdiVersion    = "0.12.0"
  lazy val logbackVersion   = "1.1.3"
  lazy val curatorVersion   = "2.10.0"
  lazy val zookeeperVersion = "3.4.7"
  lazy val nifiVersion      = "0.5.1"
  lazy val examVersion      = "4.8.0"
  lazy val scalaTestVersion = "2.2.6"
  lazy val juiVersion       = "0.11"
  
  // Libraries
  val dcsData         = "org.dcs"               % "org.dcs.data"      % dcsVersion
  val dcsApi          = "org.dcs"               % "org.dcs.api"       % dcsVersion
  val dcsTest         = "org.dcs"               % "org.dcs.test"      % dcsVersion        
  val paxCdiApi       = "org.ops4j.pax.cdi"     % "pax-cdi-api"       % paxCdiVersion
  val logbackCore     = "ch.qos.logback"        % "logback-core"      % logbackVersion
	val logbackClassic  =	"ch.qos.logback"        % "logback-classic"   % logbackVersion
	val curator         = "org.apache.curator"    % "curator-framework" % curatorVersion
	val zookeeper       = "org.apache.zookeeper"  % "zookeeper"         % zookeeperVersion
	val nifi            = "org.apache.nifi"       % "nifi-api"          % nifiVersion
	val scalaTest       = "org.scalatest"         %% "scalatest"        % scalaTestVersion 
	val junitInterface  = "com.novocode"          % "junit-interface"   % juiVersion


	
	
  
	// Dependencies
  val coreDependencies = Seq(
    dcsData         % "provided", 
    dcsApi          % "provided",
    paxCdiApi       % "provided",
    logbackCore     % "provided",
    logbackClassic  % "provided",    
    curator                       exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-api"),
    zookeeper                     exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-api") exclude("org.slf4j", "slf4j-log4j12"),
    nifi,
    dcsTest         % "test",
    scalaTest       % "test",
    junitInterface  % "test"
    )  
}