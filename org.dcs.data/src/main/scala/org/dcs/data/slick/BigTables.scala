package org.dcs.data.slick

import org.dcs.api.data.FlowData
import slick.collection.heterogeneous.{HList, HNil}
import org.dcs.api.processor.Attributes

/**
  * Created by cmathew on 03.03.17.
  */
object BigTables extends BigTables {
  def hListAt(hList: HList, index: Int): Any = index match {
    case 0 => hList.head
    case _ => hListAt(hList.tail, index - 1)
  }
}

trait BigTables {
  import BigTables._
  // From slick documentation (http://slick.lightbend.com/doc/3.1.0/code-generation.html)
  // 'For tables with more than 22 columns the generator automatically switches
  // to Slick’s experimental HList implementation for overcoming
  // Scala’s tuple size limit'
  // This implies that the code generation will not produce case classes
  // for tables that have more than 22 fields, but an HList

  // The workaround for this can be acheived by,
  // 1. Declaring the case classes in this trait with prefix 'Big'
  // 2. Adding the mapping code to the generated table class, e.g.
  //    class FlowDataProvenance(_tableTag: Tag) extends Table[BigTables.BigFlowDataProvenanceRow](_tableTag, "flow_data_provenance") {
  //
  //        def * = ... :: ... :: ... :: HNil <> (BigTables.BigFlowDataProvenanceRow.apply, BigTables.BigFlowDataProvenanceRow.unapply)

  object BigFlowDataProvenanceRow {
    def apply(hList: Tables.FlowDataProvenanceRow) = new BigFlowDataProvenanceRow(
      hListAt(hList, 0).asInstanceOf[String],
      hListAt(hList, 1).asInstanceOf[Long],
      hListAt(hList, 2).asInstanceOf[Option[Double]],
      hListAt(hList, 3).asInstanceOf[Option[Double]],
      hListAt(hList, 4).asInstanceOf[Option[Double]],
      hListAt(hList, 5).asInstanceOf[Option[Double]],
      hListAt(hList, 6).asInstanceOf[Option[Double]],
      hListAt(hList, 7).asInstanceOf[Option[Double]],
      hListAt(hList, 8).asInstanceOf[Option[String]],
      hListAt(hList, 9).asInstanceOf[Option[String]],
      hListAt(hList, 10).asInstanceOf[Option[String]],
      hListAt(hList, 11).asInstanceOf[Option[String]],
      hListAt(hList, 12).asInstanceOf[Option[String]],
      hListAt(hList, 13).asInstanceOf[Option[String]],
      hListAt(hList, 14).asInstanceOf[Option[String]],
      hListAt(hList, 15).asInstanceOf[Option[String]],
      hListAt(hList, 16).asInstanceOf[Option[String]],
      hListAt(hList, 17).asInstanceOf[Option[String]],
      hListAt(hList, 18).asInstanceOf[Option[String]],
      hListAt(hList, 19).asInstanceOf[Option[String]],
      hListAt(hList, 20).asInstanceOf[Option[String]],
      hListAt(hList, 21).asInstanceOf[Option[String]],
      hListAt(hList, 22).asInstanceOf[Option[String]],
      hListAt(hList, 23).asInstanceOf[Option[String]],
      hListAt(hList, 24).asInstanceOf[Option[String]])

    def unapply(row: BigFlowDataProvenanceRow)= Some(row.id ::
      row.eventId ::
      row.eventTime ::
      row.flowFileEntryDate ::
      row.lineageStartEntryDate ::
      row.fileSize ::
      row.previousFileSize ::
      row.eventDuration ::
      row.eventType ::
      row.attributes ::
      row.previousAttributes ::
      row.updatedAttributes ::
      row.componentId ::
      row.componentType ::
      row.transitUri ::
      row.sourceSystemFlowFileIdentifier ::
      row.flowFileUuid ::
      row.parentUuids ::
      row.childUuids ::
      row.alternateIdentifierUri ::
      row.details ::
      row.relationship ::
      row.sourceQueueIdentifier ::
      row.contentClaimIdentifier ::
      row.previousContentClaimIdentifier ::
      HNil)
  }





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
