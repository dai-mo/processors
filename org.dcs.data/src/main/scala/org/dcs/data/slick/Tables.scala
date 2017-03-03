package org.dcs.data.slick

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.collection.heterogeneous._
  import slick.collection.heterogeneous.syntax._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = FlowDataContent.schema ++ FlowDataProvenance.schema ++ SchemaVersion.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table FlowDataContent
   *  @param id Database column id SqlType(varchar), PrimaryKey
   *  @param claimCount Database column claim_count SqlType(int4), Default(None)
   *  @param timestamp Database column timestamp SqlType(timestamp), Default(None)
   *  @param data Database column data SqlType(bytea), Default(None) */
  case class FlowDataContentRow(id: String, claimCount: Option[Int] = None, timestamp: Option[java.sql.Timestamp] = None, data: Option[Array[Byte]] = None)
  /** GetResult implicit for fetching FlowDataContentRow objects using plain SQL queries */
  implicit def GetResultFlowDataContentRow(implicit e0: GR[String], e1: GR[Option[Int]], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[Array[Byte]]]): GR[FlowDataContentRow] = GR{
    prs => import prs._
    FlowDataContentRow.tupled((<<[String], <<?[Int], <<?[java.sql.Timestamp], <<?[Array[Byte]]))
  }
  /** Table description of table flow_data_content. Objects of this class serve as prototypes for rows in queries. */
  class FlowDataContent(_tableTag: Tag) extends Table[FlowDataContentRow](_tableTag, "flow_data_content") {
    def * = (id, claimCount, timestamp, data) <> (FlowDataContentRow.tupled, FlowDataContentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), claimCount, timestamp, data).shaped.<>({r=>import r._; _1.map(_=> FlowDataContentRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    /** Database column claim_count SqlType(int4), Default(None) */
    val claimCount: Rep[Option[Int]] = column[Option[Int]]("claim_count", O.Default(None))
    /** Database column timestamp SqlType(timestamp), Default(None) */
    val timestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("timestamp", O.Default(None))
    /** Database column data SqlType(bytea), Default(None) */
    val data: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("data", O.Default(None))
  }
  /** Collection-like TableQuery object for table FlowDataContent */
  lazy val FlowDataContent = new TableQuery(tag => new FlowDataContent(tag))

  /** Row type of table FlowDataProvenance */
  type FlowDataProvenanceRow = HCons[String,HCons[Long,HCons[Option[Double],HCons[Option[Double],HCons[Option[Double],HCons[Option[Double],HCons[Option[Double],HCons[Option[Double],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HCons[Option[String],HNil]]]]]]]]]]]]]]]]]]]]]]]]]
  /** Constructor for FlowDataProvenanceRow providing default values if available in the database schema. */
  def FlowDataProvenanceRow(id: String, eventId: Long, eventTime: Option[Double] = None, flowFileEntryDate: Option[Double] = None, lineageStartEntryDate: Option[Double] = None, fileSize: Option[Double] = None, previousFileSize: Option[Double] = None, eventDuration: Option[Double] = None, eventType: Option[String] = None, attributes: Option[String] = None, previousAttributes: Option[String] = None, updatedAttributes: Option[String] = None, componentId: Option[String] = None, componentType: Option[String] = None, transitUri: Option[String] = None, sourceSystemFlowFileIdentifier: Option[String] = None, flowFileUuid: Option[String] = None, parentUuids: Option[String] = None, childUuids: Option[String] = None, alternateIdentifierUri: Option[String] = None, details: Option[String] = None, relationship: Option[String] = None, sourceQueueIdentifier: Option[String] = None, contentClaimIdentifier: Option[String] = None, previousContentClaimIdentifier: Option[String] = None): FlowDataProvenanceRow = {
    id :: eventId :: eventTime :: flowFileEntryDate :: lineageStartEntryDate :: fileSize :: previousFileSize :: eventDuration :: eventType :: attributes :: previousAttributes :: updatedAttributes :: componentId :: componentType :: transitUri :: sourceSystemFlowFileIdentifier :: flowFileUuid :: parentUuids :: childUuids :: alternateIdentifierUri :: details :: relationship :: sourceQueueIdentifier :: contentClaimIdentifier :: previousContentClaimIdentifier :: HNil
  }
  /** GetResult implicit for fetching FlowDataProvenanceRow objects using plain SQL queries */
  implicit def GetResultFlowDataProvenanceRow(implicit e0: GR[String], e1: GR[Long], e2: GR[Option[Double]], e3: GR[Option[String]]): GR[FlowDataProvenanceRow] = GR{
    prs => import prs._
    <<[String] :: <<[Long] :: <<?[Double] :: <<?[Double] :: <<?[Double] :: <<?[Double] :: <<?[Double] :: <<?[Double] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: <<?[String] :: HNil
  }
  /** Table description of table flow_data_provenance. Objects of this class serve as prototypes for rows in queries. */
  class FlowDataProvenance(_tableTag: Tag) extends Table[BigTables.BigFlowDataProvenanceRow](_tableTag, "flow_data_provenance") {
    import shapeless.Generic
    import shapeless.HNil
    import slickless._

    def * = (id :: eventId :: eventTime :: flowFileEntryDate :: lineageStartEntryDate :: fileSize :: previousFileSize :: eventDuration :: eventType :: attributes :: previousAttributes :: updatedAttributes :: componentId :: componentType :: transitUri :: sourceSystemFlowFileIdentifier :: flowFileUuid :: parentUuids :: childUuids :: alternateIdentifierUri :: details :: relationship :: sourceQueueIdentifier :: contentClaimIdentifier :: previousContentClaimIdentifier :: HNil).mappedWith(Generic[BigTables.BigFlowDataProvenanceRow])

    /** Database column id SqlType(varchar), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    /** Database column event_id SqlType(bigserial), AutoInc */
    val eventId: Rep[Long] = column[Long]("event_id", O.AutoInc)
    /** Database column event_time SqlType(float8), Default(None) */
    val eventTime: Rep[Option[Double]] = column[Option[Double]]("event_time", O.Default(None))
    /** Database column flow_file_entry_date SqlType(float8), Default(None) */
    val flowFileEntryDate: Rep[Option[Double]] = column[Option[Double]]("flow_file_entry_date", O.Default(None))
    /** Database column lineage_start_entry_date SqlType(float8), Default(None) */
    val lineageStartEntryDate: Rep[Option[Double]] = column[Option[Double]]("lineage_start_entry_date", O.Default(None))
    /** Database column file_size SqlType(float8), Default(None) */
    val fileSize: Rep[Option[Double]] = column[Option[Double]]("file_size", O.Default(None))
    /** Database column previous_file_size SqlType(float8), Default(None) */
    val previousFileSize: Rep[Option[Double]] = column[Option[Double]]("previous_file_size", O.Default(None))
    /** Database column event_duration SqlType(float8), Default(None) */
    val eventDuration: Rep[Option[Double]] = column[Option[Double]]("event_duration", O.Default(None))
    /** Database column event_type SqlType(varchar), Default(None) */
    val eventType: Rep[Option[String]] = column[Option[String]]("event_type", O.Default(None))
    /** Database column attributes SqlType(varchar), Default(None) */
    val attributes: Rep[Option[String]] = column[Option[String]]("attributes", O.Default(None))
    /** Database column previous_attributes SqlType(varchar), Default(None) */
    val previousAttributes: Rep[Option[String]] = column[Option[String]]("previous_attributes", O.Default(None))
    /** Database column updated_attributes SqlType(varchar), Default(None) */
    val updatedAttributes: Rep[Option[String]] = column[Option[String]]("updated_attributes", O.Default(None))
    /** Database column component_id SqlType(varchar), Default(None) */
    val componentId: Rep[Option[String]] = column[Option[String]]("component_id", O.Default(None))
    /** Database column component_type SqlType(varchar), Default(None) */
    val componentType: Rep[Option[String]] = column[Option[String]]("component_type", O.Default(None))
    /** Database column transit_uri SqlType(varchar), Default(None) */
    val transitUri: Rep[Option[String]] = column[Option[String]]("transit_uri", O.Default(None))
    /** Database column source_system_flow_file_identifier SqlType(varchar), Default(None) */
    val sourceSystemFlowFileIdentifier: Rep[Option[String]] = column[Option[String]]("source_system_flow_file_identifier", O.Default(None))
    /** Database column flow_file_uuid SqlType(varchar), Default(None) */
    val flowFileUuid: Rep[Option[String]] = column[Option[String]]("flow_file_uuid", O.Default(None))
    /** Database column parent_uuids SqlType(varchar), Default(None) */
    val parentUuids: Rep[Option[String]] = column[Option[String]]("parent_uuids", O.Default(None))
    /** Database column child_uuids SqlType(varchar), Default(None) */
    val childUuids: Rep[Option[String]] = column[Option[String]]("child_uuids", O.Default(None))
    /** Database column alternate_identifier_uri SqlType(varchar), Default(None) */
    val alternateIdentifierUri: Rep[Option[String]] = column[Option[String]]("alternate_identifier_uri", O.Default(None))
    /** Database column details SqlType(varchar), Default(None) */
    val details: Rep[Option[String]] = column[Option[String]]("details", O.Default(None))
    /** Database column relationship SqlType(varchar), Default(None) */
    val relationship: Rep[Option[String]] = column[Option[String]]("relationship", O.Default(None))
    /** Database column source_queue_identifier SqlType(varchar), Default(None) */
    val sourceQueueIdentifier: Rep[Option[String]] = column[Option[String]]("source_queue_identifier", O.Default(None))
    /** Database column content_claim_identifier SqlType(varchar), Default(None) */
    val contentClaimIdentifier: Rep[Option[String]] = column[Option[String]]("content_claim_identifier", O.Default(None))
    /** Database column previous_content_claim_identifier SqlType(varchar), Default(None) */
    val previousContentClaimIdentifier: Rep[Option[String]] = column[Option[String]]("previous_content_claim_identifier", O.Default(None))

    /** Index over (componentId) (database name component_id) */
    val index1 = index("component_id", componentId :: HNil)
    /** Index over (eventId) (database name event_id) */
    val index2 = index("event_id", eventId :: HNil)
    /** Index over (eventType) (database name event_type) */
    val index3 = index("event_type", eventType :: HNil)
    /** Index over (flowFileUuid) (database name flow_file_uuid) */
    val index4 = index("flow_file_uuid", flowFileUuid :: HNil)
    /** Index over (relationship) (database name relationship) */
    val index5 = index("relationship", relationship :: HNil)
  }
  /** Collection-like TableQuery object for table FlowDataProvenance */
  lazy val FlowDataProvenance = new TableQuery(tag => new FlowDataProvenance(tag))

  /** Entity class storing rows of table SchemaVersion
   *  @param installedRank Database column installed_rank SqlType(int4), PrimaryKey
   *  @param version Database column version SqlType(varchar), Length(50,true), Default(None)
   *  @param description Database column description SqlType(varchar), Length(200,true)
   *  @param `type` Database column type SqlType(varchar), Length(20,true)
   *  @param script Database column script SqlType(varchar), Length(1000,true)
   *  @param checksum Database column checksum SqlType(int4), Default(None)
   *  @param installedBy Database column installed_by SqlType(varchar), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(timestamp)
   *  @param executionTime Database column execution_time SqlType(int4)
   *  @param success Database column success SqlType(bool) */
  case class SchemaVersionRow(installedRank: Int, version: Option[String] = None, description: String, `type`: String, script: String, checksum: Option[Int] = None, installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching SchemaVersionRow objects using plain SQL queries */
  implicit def GetResultSchemaVersionRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Int]], e4: GR[java.sql.Timestamp], e5: GR[Boolean]): GR[SchemaVersionRow] = GR{
    prs => import prs._
    SchemaVersionRow.tupled((<<[Int], <<?[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table schema_version. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class SchemaVersion(_tableTag: Tag) extends Table[SchemaVersionRow](_tableTag, "schema_version") {
    def * = (installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (SchemaVersionRow.tupled, SchemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(installedRank), version, Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> SchemaVersionRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column installed_rank SqlType(int4), PrimaryKey */
    val installedRank: Rep[Int] = column[Int]("installed_rank", O.PrimaryKey)
    /** Database column version SqlType(varchar), Length(50,true), Default(None) */
    val version: Rep[Option[String]] = column[Option[String]]("version", O.Length(50,varying=true), O.Default(None))
    /** Database column description SqlType(varchar), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(varchar), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(varchar), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(int4), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))
    /** Database column installed_by SqlType(varchar), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(timestamp) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(int4) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(bool) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (success) (database name schema_version_s_idx) */
    val index1 = index("schema_version_s_idx", success)
  }
  /** Collection-like TableQuery object for table SchemaVersion */
  lazy val SchemaVersion = new TableQuery(tag => new SchemaVersion(tag))
}
