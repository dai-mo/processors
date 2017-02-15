import sbt._

object Dependencies {
	// Versions
	lazy val dcsApiVersion    		 = "0.3.0-SNAPSHOT"
	lazy val dcsCommonsVersion     = "0.2.0-SNAPSHOT"
	lazy val dcsTestVersion   		 = "0.1.0"
	lazy val paxCdiVersion    		 = "0.12.0"
	lazy val cdiApiVersion    		 = "1.2"
	lazy val logbackVersion   		 = "1.1.3"
	lazy val curatorVersion   		 = "2.10.0"
	lazy val zookeeperVersion 		 = "3.4.7"
	lazy val nifiVersion      		 = "0.5.1"
	lazy val examVersion      		 = "4.8.0"
	lazy val scalaTestVersion 		 = "2.2.6"
	lazy val juiVersion       		 = "0.11"
	lazy val openCsvVersion   		 = "3.8"
	lazy val jacksonVersion   		 = "2.4.5"
	lazy val jaxRsVersion     		 = "2.0.1"
	lazy val sqliteVersion    		 = "3.8.11.2"
	lazy val avroVersion 					 = "1.8.1"
	lazy val quillCassandraVersion = "1.0.0"
	lazy val scalaReflectVersion 	 = "2.11.7"
  lazy val guavaVersion          = "18.0"
	lazy val quillVersion          = "1.0.0"
	lazy val quillJdbcVersion      = "1.0.1"
	lazy val dataStaxDriverVersion = "3.1.0"
	lazy val postgresDriverVersion = "9.4.1208"
  lazy val slickVersion          = "3.1.1"


	// Libraries
	val dcsApi          = "org.dcs"                    % "org.dcs.api"             % dcsApiVersion
  val dcsCommons      = "org.dcs"                    % "org.dcs.commons"         % dcsCommonsVersion

	val paxCdiApi       = "org.ops4j.pax.cdi"          % "pax-cdi-api"             % paxCdiVersion
	val cdiApi          = "javax.enterprise"           % "cdi-api"                 % cdiApiVersion
	val logbackCore     = "ch.qos.logback"             % "logback-core"            % logbackVersion
	val logbackClassic  =	"ch.qos.logback"             % "logback-classic"         % logbackVersion

  val avro            = "org.apache.avro"            % "avro"                    % avroVersion
	val scalaReflect    = "org.scala-lang"             % "scala-reflect"           % scalaReflectVersion
  val guava           = "com.google.guava"           % "guava"                   % guavaVersion
	val openCsv         = "com.opencsv"                % "opencsv"                 % openCsvVersion

	val quillCassandra  = "io.getquill"                %% "quill-cassandra"        % quillVersion
	val quillJdbc       = "io.getquill"                %% "quill-jdbc"             % quillJdbcVersion
	val datastaxDriver  = "com.datastax.cassandra"     % "cassandra-driver-core"   % dataStaxDriverVersion
	val postgresDriver  = "org.postgresql"             % "postgresql"              % postgresDriverVersion
  val slick           = "com.typesafe.slick"         %% "slick"                  % slickVersion
  val slickHikariCP   = "com.typesafe.slick"         %% "slick-hikaricp"         % slickVersion
  val slickCodeGen    = "com.typesafe.slick"         %% "slick-codegen"          % slickVersion


	val dcsTest         = "org.dcs"                    % "org.dcs.test"            % dcsTestVersion
	val scalaTest       = "org.scalatest"              %% "scalatest"              % scalaTestVersion
	val junitInterface  = "com.novocode"               % "junit-interface"   			 % juiVersion

	// Dependencies
	val coreDependencies = Seq(
		dcsApi          % "provided",
    dcsCommons      % "provided",
		avro            % "provided",
		paxCdiApi       % "provided",
		logbackCore     % "provided",
		logbackClassic  % "provided",
    cdiApi          % "provided",
		openCsv,
    guava,

		dcsTest         % "test",
		scalaTest       % "test",
		junitInterface  % "test"
	)

	def dataDependencies(databaseLib: String): Seq[ModuleID] = Seq(
		dcsApi          % "provided",
		dcsCommons      % "provided",

		dcsTest         % "test",
		scalaTest       % "test",
		junitInterface  % "test"
	) ++ quillDependecies(databaseLib)

	def quillDependecies(databaseLib: String): Seq[ModuleID] = databaseLib match {
		case "quill-cassandra" => Seq(quillCassandra, datastaxDriver)
		case "quill-postgres" => Seq(quillJdbc, postgresDriver)
    case "slick-postgres" => Seq(slick, slickHikariCP, slickCodeGen, postgresDriver)
    case _ => throw new IllegalStateException("Target DB lib " + databaseLib + " is not recognised. \n" +
      "Should be one of quill-cassandra , quill-postgres, slick-postgres")
	}

}
