import sbt._

object Dependencies {
	// Versions
	lazy val scVersion             = "2.11.7"
	lazy val dcsApiVersion    		 = "0.4.0"
	lazy val dcsCommonsVersion     = "0.3.0"
	lazy val dcsKaaClientVersion   = "0.3.0-SNAPSHOT"

	lazy val paxCdiVersion    		 = "0.12.0"
	lazy val cdiApiVersion    		 = "1.2"
	lazy val logbackVersion   		 = "1.1.3"
	lazy val curatorVersion   		 = "2.10.0"
	lazy val zookeeperVersion 		 = "3.4.7"
	lazy val examVersion      		 = "4.8.0"
	lazy val scalaTestVersion 		 = "3.0.0"
	lazy val juiVersion       		 = "0.11"
	lazy val openCsvVersion   		 = "3.8"
	lazy val jacksonVersion   		 = "2.4.5"
	lazy val jaxRsVersion     		 = "2.0.1"
	lazy val sqliteVersion    		 = "3.8.11.2"
	lazy val avroVersion 					 = "1.8.1"
	lazy val jerseyVersion  			 = "2.22.1"
	lazy val quillCassandraVersion = "1.0.0"
  lazy val guavaVersion          = "18.0"
	lazy val quillVersion          = "1.0.0"
	lazy val quillJdbcVersion      = "1.0.1"
	lazy val dataStaxDriverVersion = "3.1.0"
	lazy val postgresDriverVersion = "9.4.1212"
  lazy val slickVersion          = "3.1.1"
	lazy val shapelessVersion      = "2.3.1"
	lazy val slicklessVersion      = "0.3.0"
	lazy val sparkVersion          = "2.2.0"
	lazy val mockitoVersion        = "1.10.19"

  // FIXME: Currently we have duplicate entries for
  //        typesafeConfig in here and in the
  //        project/build.sbt used for the build itself
	lazy val typesafeConfigVersion = "1.3.1"
  lazy val flywayVersion         = "4.0.3"


	// Libraries

	val dcsApi          = "org.dcs"                    % "org.dcs.api"             % dcsApiVersion
  val dcsCommons      = "org.dcs"                    % "org.dcs.commons"         % dcsCommonsVersion
	val dcsKaaClient    = "org.dcs"                    % "org.dcs.iot.kaa.client"  % dcsKaaClientVersion

	val paxCdiApi       = "org.ops4j.pax.cdi"          % "pax-cdi-api"             % paxCdiVersion
	val cdiApi          = "javax.enterprise"           % "cdi-api"                 % cdiApiVersion
	val logbackCore     = "ch.qos.logback"             % "logback-core"            % logbackVersion
	val logbackClassic  =	"ch.qos.logback"             % "logback-classic"         % logbackVersion

  val avro            = "org.apache.avro"            % "avro"                    % avroVersion
	val guava           = "com.google.guava"           % "guava"                   % guavaVersion
	val openCsv         = "com.opencsv"                % "opencsv"                 % openCsvVersion
	val jerseyMultipart = "org.glassfish.jersey.media" % "jersey-media-multipart"  % jerseyVersion


	val sparkLauncher 	= "org.apache.spark" 					 %% "spark-launcher" 				 % sparkVersion


	val quillCassandra  = "io.getquill"                %% "quill-cassandra"        % quillVersion
	val quillJdbc       = "io.getquill"                %% "quill-jdbc"             % quillJdbcVersion
	val datastaxDriver  = "com.datastax.cassandra"     %  "cassandra-driver-core"  % dataStaxDriverVersion
	val postgresDriver  = "org.postgresql"             %  "postgresql"             % postgresDriverVersion
  val slick           = "com.typesafe.slick"         %% "slick"                  % slickVersion
  val slickHikariCP   = "com.typesafe.slick"         %% "slick-hikaricp"         % slickVersion
  val slickCodeGen    = "com.typesafe.slick"         %% "slick-codegen"          % slickVersion
  val flyway          = "org.flywaydb"               %  "flyway-core"            % flywayVersion
  val typesafeConfig  = "com.typesafe"               %  "config"                 % typesafeConfigVersion

	val scalaTest       = "org.scalatest"              %% "scalatest"              % scalaTestVersion
	val junitInterface  = "com.novocode"               % "junit-interface"   			 % juiVersion
	val mockitoAll      = "org.mockito"                % "mockito-all"             % mockitoVersion

	// Dependencies
	val coreDependencies: Seq[ModuleID] = Seq(
		dcsApi          % "provided",
    dcsCommons      % "provided",

		avro            % "provided",
		paxCdiApi       % "provided",
		logbackCore     % "provided",
		logbackClassic  % "provided",
    cdiApi          % "provided",
		openCsv,
    guava,
		dcsKaaClient,

    sparkLauncher,

		scalaTest       % "test",
		junitInterface  % "test",
		mockitoAll      % "test"
	)

	val dataDependencies: Seq[ModuleID] = Seq(
		dcsApi          % "provided",
		dcsCommons      % "provided",
		typesafeConfig,
    slick,
    // FIXME: This should be updated once the
    //        slick-hikaricp osgi manifest issue,
    //        https://github.com/slick/slick/issues/1694
    //        is resolved
		slickHikariCP   % "provided",
    "com.zaxxer" % "HikariCP-java6" % "2.3.7",

    slickCodeGen,
    postgresDriver,
    flyway,
		logbackCore     % "provided",
		logbackClassic  % "provided",


		scalaTest       % "test",
		junitInterface  % "test",
		mockitoAll      % "test"
	)



}
