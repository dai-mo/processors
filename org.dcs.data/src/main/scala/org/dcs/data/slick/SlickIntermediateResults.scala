package org.dcs.data.slick


import java.util.{List => JavaList}

import org.dcs.api.processor.RelationshipType
import org.dcs.api.service.Provenance
import org.dcs.data.IntermediateResultsAdapter
import slick.driver.JdbcDriver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future




/**
  * Created by cmathew on 17.02.17.
  */
class SlickIntermediateResults(val driver: JdbcDriver, dbType: String) extends IntermediateResultsAdapter {
  import driver.api._
  val db = Database.forConfig(dbType, classLoader = this.getClass.getClassLoader)

  private[this] var _logQueries: Boolean = false

  private def logQueries: Boolean = _logQueries

  private def logQueries_=(value: Boolean): Unit = {
    _logQueries = value
  }

  private def uniqueForId[T](seq: Seq[T], id : String): Option[T] = {
    if(seq.size > 1)
      throw new IllegalStateException("More than one content record for given id " + id)
    seq.headOption
  }

  private def getContentQuery(contentId : Rep[String]) = {
    Tables.FlowDataContent.filter(_.id === contentId)
  }

  private lazy val getContentQueryCompiled = Compiled(getContentQuery _)

  override def getContent(contentId : String): Future[Option[Tables.FlowDataContentRow]] = {
    val contentAction = getContentQueryCompiled(contentId).result
    db.run(contentAction).map(uniqueForId(_, contentId))
  }


  override def createContent(fdc: Tables.FlowDataContentRow): Future[Unit] = {
    val createContentAction = DBIO.seq(
      Tables.FlowDataContent += fdc
    )

    db.run(createContentAction)
  }

  private def contentDataQuery(contentId : Rep[String]) = {
    Tables.FlowDataContent.filter(_.id === contentId).map(_.data)
  }

  private lazy val contentDataQueryCompiled = Compiled(contentDataQuery _)

  override def updateDataContent(contentId : String, data: Array[Byte]): Future[Int] = {
    db.run(contentDataQueryCompiled(contentId).update(Option(data)))
  }

  private def claimaintCountQuery(contentId : Rep[String]) = {
    Tables.FlowDataContent.filter(_.id === contentId).map(_.claimCount)
  }

  private lazy val claimaintCountQueryCompiled = Compiled(claimaintCountQuery _)

  private def updateClaimaintCount(contentId : String, updateBy: Int): Future[Option[Int]] = {

    var updatedClaimCount: Option[Int] = None
    val incrementClaimaintCountAction = getContentQueryCompiled(contentId).result.
      flatMap(content => {
        updatedClaimCount = Some(content.headOption.get.claimCount.get + updateBy)
        claimaintCountQueryCompiled(contentId).update(updatedClaimCount)
      })

    db.run(incrementClaimaintCountAction).
      map {
        case 0 => None
        case _ => updatedClaimCount
      }
  }

  override def incrementClaimaintCount(contentId : String): Future[Option[Int]] = updateClaimaintCount(contentId, 1)
  override def decrementClaimaintCount(contentId : String): Future[Option[Int]] = updateClaimaintCount(contentId, -1)


  override def getClaimantCount(contentId: String): Future[Option[Int]] = {
    db.run(claimaintCountQueryCompiled(contentId).result).
      map(uniqueForId(_, contentId).get)
  }

  override def getContentSize: Future[Int] = {
    db.run(Tables.FlowDataContent.length.result)
  }

  override def deleteContent(contentId: String): Future[Int] = {
    db.run(getContentQueryCompiled(contentId).delete)
  }

  override def purgeContent(): Future[Int] = db.run(Tables.FlowDataContent.delete)

  // FIXME : Add method for bulk insert
  //         http://stackoverflow.com/questions/35001493/slick-3-0-bulk-insert-or-update
  override def createProvenance(fdp: BigTables.BigFlowDataProvenanceRow): Future[Unit] = {
    val createProvenance = DBIO.seq(Tables.FlowDataProvenance += fdp)
    db.run(createProvenance)
  }

  private def getProvenanceEventsQuery(maxResults: ConstColumn[Long]) = {
    Tables.FlowDataProvenance.take(maxResults)
  }

  private lazy val getProvenanceEventsQueryCompiled = Compiled(getProvenanceEventsQuery _)

  override def getProvenanceEvents(maxResults: Long): Future[List[BigTables.BigFlowDataProvenanceRow]] = {
    db.run(getProvenanceEventsQueryCompiled(maxResults).result).map(_.toList)
  }

  private def getProvenanceByComponentIdQuery(cid: Rep[String], maxResults: ConstColumn[Long]) = {
    (for {
      provenance <- Tables.FlowDataProvenance if provenance.componentId === cid && provenance.eventType =!= "DROP"
      content <- Tables.FlowDataContent if provenance.contentClaimIdentifier === content.id

    } yield (content.id, "", "", content.data, content.timestamp, provenance.relationship))
      .sortBy(_._5.desc).take(maxResults)
  }

  private lazy val getProvenanceByComponentIdQueryCompiled = Compiled(getProvenanceByComponentIdQuery _)

  override def getProvenanceByComponentId(cid: String, maxResults: Int): Future[List[Provenance]] = {

    val provenanceByComponentIdAction = getProvenanceByComponentIdQueryCompiled(cid, maxResults).result

    (for (provList <- db.run(provenanceByComponentIdAction) ) yield provList.map(prov => {
      Provenance(prov._1, prov._2, prov._3, prov._4.getOrElse(Array[Byte]()), "", prov._5.get, prov._6.getOrElse(RelationshipType.Unknown.id))
    })).map(_.toList)
  }



  private def getProvenanceEventsByEventIdQuery(eventId: Rep[Long], maxResults: ConstColumn[Long]) = {
    Tables.FlowDataProvenance.filter(fdp =>
      fdp.eventId >= eventId && fdp.eventId < (eventId + maxResults))
  }

  private lazy val getProvenanceEventsByEventIdQueryCompiled = Compiled(getProvenanceEventsByEventIdQuery _)

  override def getProvenanceEventsByEventId(eventId: Long, maxResults: Int): Future[List[BigTables.BigFlowDataProvenanceRow]] = {
    db.run(getProvenanceEventsByEventIdQueryCompiled(eventId, maxResults).result).map(_.toList)
  }

  private def getProvenanceEventByEventIdQuery(eventId: Rep[Long]) = {
    Tables.FlowDataProvenance.filter(fdp =>fdp.eventId === eventId)
  }

  private lazy val getProvenanceEventByEventIdQueryCompiled = Compiled(getProvenanceEventByEventIdQuery _)

  override def getProvenanceEventByEventId(eventId: Long): Future[Option[BigTables.BigFlowDataProvenanceRow]] = {
    db.run(getProvenanceEventByEventIdQueryCompiled(eventId).result).map(uniqueForId(_, eventId.toString))
  }

  private def getProvenanceEventByIdQuery(id: Rep[String]) = {
    Tables.FlowDataProvenance.filter(fdp => fdp.id === id)
  }

  private lazy val getProvenanceEventByIdQueryCompiled = Compiled(getProvenanceEventByIdQuery _)

  override def getProvenanceEventById(id: String): Future[Option[BigTables.BigFlowDataProvenanceRow]] = {
    db.run(getProvenanceEventByIdQueryCompiled(id).result).map(uniqueForId(_, id))
  }

  override def getProvenanceSize: Future[Int] = {
    db.run(Tables.FlowDataProvenance.length.result)
  }

  override def getProvenanceEventsByEventType(eventType: String, maxResults: Long = 1000): Future[List[BigTables.BigFlowDataProvenanceRow]] =
    db.run(Tables.FlowDataProvenance.filter(fdp => fdp.eventType === eventType).take(maxResults).result).map(_.toList)

  override def getProvenanceEventsByFlowFileUuid(flowFileUuid: String, maxResults: Long = 1000): Future[List[BigTables.BigFlowDataProvenanceRow]] =
    db.run(Tables.FlowDataProvenance.filter(fdp => fdp.flowFileUuid === flowFileUuid).take(maxResults).result).map(_.toList)

  override def getProvenanceEventsByComponentId(componentId: String, maxResults: Long = 1000): Future[List[BigTables.BigFlowDataProvenanceRow]] =
    db.run(Tables.FlowDataProvenance.filter(fdp => fdp.componentId === componentId).take(maxResults).result).map(_.toList)

  override def getProvenanceEventsByRelationship(relationship: String, maxResults: Long = 1000): Future[List[BigTables.BigFlowDataProvenanceRow]] =
    db.run(Tables.FlowDataProvenance.filter(fdp => fdp.relationship === relationship).take(maxResults).result).map(_.toList)

  override def deleteProvenanceByComponentId(cid: String): Future[Int] = {
    // FIXME: Deleting each content by id is not optimal
    //        Need to add a foreign key constraint to enable cascade deletion
    val contentIdsAction= Tables.FlowDataProvenance.filter(_.componentId === cid).map(prov => prov.contentClaimIdentifier).result
    val contentIds = db.run(contentIdsAction)

    contentIds.foreach(ids => ids.foreach(id => db.run(Tables.FlowDataContent.filter(_.id === id.get).delete)))

    val deleteProvenanceAction = Tables.FlowDataProvenance.filter(_.componentId === cid).delete
    db.run(deleteProvenanceAction)
  }

  override def getProvenanceMaxEventId(): Future[Option[Long]] = {
    db.run(Tables.FlowDataProvenance.map(_.eventId).max.result)
  }

  override def purgeProvenance(): Future[Int] = db.run(Tables.FlowDataProvenance.delete)

  override def purge(): Future[Int] = {
    val purgeFdcAction = Tables.FlowDataContent.delete
    val purgeFdpAction = Tables.FlowDataProvenance.delete
    db.run((purgeFdcAction andThen purgeFdpAction).transactionally)
  }

  override def closeDbConnection(): Unit = {
    db.close()
  }
}


