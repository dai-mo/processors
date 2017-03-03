package org.dcs.data.slick

/**
  * Created by cmathew on 03.03.17.
  */
object BigTables extends BigTables

trait BigTables {
  // From slick documentation (http://slick.lightbend.com/doc/3.1.0/code-generation.html)
  // 'For tables with more than 22 columns the generator automatically switches
  // to Slick’s experimental HList implementation for overcoming
  // Scala’s tuple size limit'
  // This implies that the code generation will not produce case classes
  // for tables that have more than 22 fields, but an HList
  // The slickless (https://github.com/underscoreio/slickless) officially
  // recognised by slick (https://github.com/slick/slick.github.com/pull/24)
  // can be used to map the HList to a case class
  // This is done by,
  // 1. Declaring the case classes in this trait with prefix 'Big'
  // 2. Adding the mapping code to the generated table class, e.g.
  //    class FlowDataProvenance(_tableTag: Tag) extends Table[BigTables.BigFlowDataProvenanceRow](_tableTag, "flow_data_provenance") {
  //        import shapeless.Generic
  //        import shapeless.HNil
  //        import slickless._
  //        def * = (... :: ... :: ... :: HNil).mappedWith(Generic[BigFlowDataProvenanceRow])

  case class BigFlowDataProvenanceRow(id: String,
                                      eventId: Long,
                                      eventTime: Option[Double],
                                      flowFileEntryDate: Option[Double],
                                      lineageStartEntryDate: Option[Double],
                                      fileSize: Option[Double],
                                      previousFileSize: Option[Double],
                                      eventDuration: Option[Double],
                                      eventType: Option[String],
                                      attributes: Option[String],
                                      previousAttributes: Option[String],
                                      updatedAttributes: Option[String],
                                      componentId: Option[String],
                                      componentType: Option[String],
                                      transitUri: Option[String],
                                      sourceSystemFlowFileIdentifier: Option[String],
                                      flowFileUuid: Option[String],
                                      parentUuids: Option[String],
                                      childUuids: Option[String],
                                      alternateIdentifierUri: Option[String],
                                      details: Option[String],
                                      relationship: Option[String],
                                      sourceQueueIdentifier: Option[String],
                                      contentClaimIdentifier: Option[String],
                                      previousContentClaimIdentifier: Option[String])

}
