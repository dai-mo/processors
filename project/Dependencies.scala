import sbt._

object Dependencies {
	    // Versions
	    lazy val dcsVersion           = "1.0.0-SNAPSHOT"
			lazy val paxCdiVersion    = "0.12.0"
			lazy val cdiApiVersion    = "1.2"
			lazy val logbackVersion   = "1.1.3"
			lazy val curatorVersion   = "2.10.0"
			lazy val zookeeperVersion = "3.4.7"
			lazy val nifiVersion      = "0.5.1"
			lazy val examVersion      = "4.8.0"
			lazy val scalaTestVersion = "2.2.6"
			lazy val juiVersion       = "0.11"
			lazy val openCsvVersion   = "3.6"
			lazy val jacksonVersion   = "2.4.5"
			lazy val jaxRsVersion     = "2.0.1"
			lazy val sqliteVersion    = "3.8.11.2"
			lazy val scalaReflectVersion = "2.11.8"

			// Libraries
			val dcsData         = "org.dcs"                    % "org.dcs.data"            % dcsVersion
			val dcsApi          = "org.dcs"                    % "org.dcs.api"             % dcsVersion
			val dcsTest         = "org.dcs"                    % "org.dcs.test"            % dcsVersion        
			val paxCdiApi       = "org.ops4j.pax.cdi"          % "pax-cdi-api"             % paxCdiVersion
			val cdiApi          = "javax.enterprise"           % "cdi-api"                 % cdiApiVersion
			val logbackCore     = "ch.qos.logback"             % "logback-core"            % logbackVersion
			val logbackClassic  =	"ch.qos.logback"             % "logback-classic"         % logbackVersion
			val curator         = "org.apache.curator"         % "curator-framework"       % curatorVersion
			val zookeeper       = "org.apache.zookeeper"       % "zookeeper"               % zookeeperVersion
			val nifi            = "org.apache.nifi"            % "nifi-api"                % nifiVersion
			val opencsv         = "com.opencsv"                % "opencsv"                 % openCsvVersion
			val jksonDatabind   = "com.fasterxml.jackson.core" % "jackson-databind"        % jacksonVersion
			val jksonCore       = "com.fasterxml.jackson.core" % "jackson-core"            % jacksonVersion
			val jaxRs           = "javax.ws.rs"                % "javax.ws.rs-api"         % jaxRsVersion
			val sqlite          = "org.xerial"                 % "sqlite-jdbc"             % sqliteVersion
			val scalaReflect    = "org.scala-lang"             % "scala-reflect"           % scalaReflectVersion
						
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
					scalaReflect,
					
					dcsTest         % "test",
					scalaTest       % "test",
					junitInterface  % "test"
					)  

			val dataDependencies = Seq(
			    dcsApi          % "provided",
					paxCdiApi       % "provided",
					cdiApi,
					logbackCore     % "provided",
					logbackClassic  % "provided",    
					curator                       exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-api"),
					zookeeper                     exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-api") exclude("org.slf4j", "slf4j-log4j12"),
					nifi,
					opencsv,
					jksonDatabind,
					jksonCore,
					jaxRs,
					sqlite,
					
					dcsTest         % "test",
					//scalaTest       % "test",
					junitInterface  % "test"
					)  
}