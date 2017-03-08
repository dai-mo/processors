package org.dcs.data

import org.dcs.api.service.Provenance
import org.dcs.data.slick.{BigTables, Tables}

import scala.concurrent.Future

/**
  * Created by cmathew on 15.02.17.
  */
trait IntermediateResultsAdapter {

  def getContent(contentId : String): Future[Option[Tables.FlowDataContentRow]]
  def createContent(fdc: Tables.FlowDataContentRow): Future[Unit]
  def updateDataContent(contentId : String, data: Array[Byte]): Future[Int]
  def incrementClaimaintCount(contentId : String): Future[Option[Int]]
  def decrementClaimaintCount(contentId : String): Future[Option[Int]]
  def getClaimantCount(contentId: String): Future[Option[Int]]
  def getContentSize: Future[Int]
  def deleteContent(contentId: String): Future[Int]
  def purgeContent(): Future[Int]


  def createProvenance(fdp: BigTables.BigFlowDataProvenanceRow): Future[Unit]
  def getProvenanceEvents(maxResults: Long): Future[List[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceByComponentId(cid: String, maxResults: Int): Future[List[Provenance]]
  def getProvenanceEventsByEventId(eventId: Long, maxResults: Int): Future[List[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceEventByEventId(eventId: Long): Future[Option[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceEventById(id: String): Future[Option[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceEventsByEventType(eventType: String, maxResults: Long): Future[List[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceEventsByFlowFileUuid(flowFileUuid: String, maxResults: Long): Future[List[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceEventsByComponentId(componentId: String, maxResults: Long): Future[List[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceEventsByRelationship(relationship: String, maxResults: Long): Future[List[BigTables.BigFlowDataProvenanceRow]]
  def getProvenanceSize: Future[Int]
  def getProvenanceMaxEventId(): Future[Option[Long]]
  def deleteProvenanceByComponentId(cid: String): Future[Int]
  def purgeProvenance(): Future[Int]

  def purge(): Future[Int]
  def closeDbConnection(): Unit
}
