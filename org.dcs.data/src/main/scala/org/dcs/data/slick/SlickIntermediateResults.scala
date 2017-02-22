package org.dcs.data.slick


import java.sql.Timestamp
import java.util
import java.util.{List => JavaList}

import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.Provenance
import org.dcs.data.IntermediateResultsAdapter
import slick.driver.JdbcDriver

import scala.collection.JavaConverters._
import scala.collection.immutable.ListMap
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

  override def createContent(fdc: FlowDataContent): Future[Unit] = {
    val contentRow = Tables.FlowDataContentRow(fdc.id,
      Option(fdc.claimCount),
      Option(new Timestamp(fdc.timestamp.getTime)),
      Option(fdc.data))

    val createContentAction = DBIO.seq(
      Tables.FlowDataContent += contentRow
    )

    db.run(createContentAction)
  }

  override def createProvenance(fdp: FlowDataProvenance): Future[Unit] = {
    val provenanceRow = Tables.FlowDataProvenanceRow(fdp.id,
      fdp.eventId.toLong,
      Option(fdp.eventTime),
      Option(fdp.flowFileEntryDate),
      Option(fdp.lineageStartEntryDate),
      Option(fdp.fileSize),
      Option(fdp.previousFileSize),
      Option(fdp.eventDuration),
      Option(fdp.eventType),
      Option(fdp.attributes),
      Option(fdp.previousAttributes),
      Option(fdp.updatedAttributes),
      Option(fdp.componentId),
      Option(fdp.componentType),
      Option(fdp.transitUri),
      Option(fdp.sourceSystemFlowFileIdentifier),
      Option(fdp.flowFileUuid),
      Option(fdp.parentUuids),
      Option(fdp.childUuids),
      Option(fdp.alternateIdentifierUri),
      Option(fdp.details),
      Option(fdp.relationship),
      Option(fdp.sourceQueueIdentifier),
      Option(fdp.contentClaimIdentifier),
      Option(fdp.previousContentClaimIdentifier))

    val createProvenance = DBIO.seq(
      Tables.FlowDataProvenance += provenanceRow
    )

    db.run(createProvenance)
  }

  private def provenanceByComponentIdQuery(cid: Rep[String], maxResults: ConstColumn[Long]) = {
    (for {
      provenance <- Tables.FlowDataProvenance if provenance.componentId === cid
      content <- Tables.FlowDataContent if provenance.contentClaimIdentifier === content.id

    } yield (content.id, "", "", content.data, content.timestamp, provenance.attributes, provenance.updatedAttributes))
      .sortBy(_._5.desc).take(maxResults)
  }

  val provenanceByComponentIdQueryCompiled = Compiled(provenanceByComponentIdQuery _)

  override def listProvenanceByComponentId(cid: String, maxResults: Int): Future[util.List[Provenance]] = {
    import org.dcs.api.data.FlowData._

    val provenanceByComponentIdAction = provenanceByComponentIdQueryCompiled(cid, maxResults).result
    (for (provList <- db.run(provenanceByComponentIdAction) ) yield provList.map(prov => {
      // FIXME: The avro deserialisation should finally move to the client side,
      //        with the schema store exposed as a service
      val updatedSchema = stringToMap(prov._7.getOrElse("")).get(RemoteProcessor.SchemaIdKey)
      val schema: String =
        if(updatedSchema.isEmpty)
          stringToMap(prov._6.getOrElse("")).getOrElse(RemoteProcessor.SchemaIdKey, "")
        else
          updatedSchema.get
      Provenance(prov._1, prov._2, prov._3, prov._4.getOrElse(Array[Byte]()), "", schema, prov._5.get)
    })).map(_.toList.asJava)
  }

  override def deleteProvenanceByComponentId(cid: String): Future[Int] = {
    // FIXME: Deleting each content by id is not optimal
    //        Need to add a foreign key constraint to enable cascade deletion
    val contentIdsAction= Tables.FlowDataProvenance.filter(_.componentId === cid).map(prov => prov.contentClaimIdentifier).result
    val contentIds = db.run(contentIdsAction)

    contentIds.foreach(ids => ids.foreach(id => db.run(Tables.FlowDataContent.filter(_.id === id.get).delete)))

    val deleteProvenanceAction = Tables.FlowDataProvenance.filter(_.componentId === cid).delete
    db.run(deleteProvenanceAction)
  }

  override def purge(): Future[Int] = {
    val purgeFdcAction = Tables.FlowDataContent.delete
    val purgeFdpAction = Tables.FlowDataProvenance.delete
    db.run((purgeFdcAction andThen purgeFdpAction).transactionally)

  }
}


