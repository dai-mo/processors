package org.dcs.data

import java.sql.Timestamp
import java.time.Instant
import java.util.{Date, UUID}

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.processor.RemoteProcessor
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.data.slick.{SlickPostgresIntermediateResults, Tables}
import org.scalatest.{Assertion, Ignore}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
/**
  * Created by cmathew on 02.02.17.
  */
@Ignore
class SlickPostgresIntermediateResultsSpec extends SlickPostgresIntermediateResultsBehaviour {


  override def beforeAll() = {
    AvroSchemaStore.add("user")
    DbMigration.migratePostgres()
  }

  override def afterAll() = {
    Await.result(SlickPostgresIntermediateResults.purge(), Duration.Inf)
    Await.result(purgeContentProvenance(SlickPostgresIntermediateResults), Duration.Inf)
  }

  "Intermediate Results" should "be created and retrieved consistently" in {
    createContentProvenance(SlickPostgresIntermediateResults)
  }

  "Content claim count" should "increment / decrement correctly" in {
   incDecContentClaimCount(SlickPostgresIntermediateResults)
  }

  "Content" should "be deleted correctly" in {
    deleteContent(SlickPostgresIntermediateResults)
  }

  "Provenance" should "retrieved consistently" in {
    queryProvenanceEvents(SlickPostgresIntermediateResults)
  }

}

trait SlickPostgresIntermediateResultsBehaviour extends AsyncDataUnitSpec {
  private val FirstName = "Obi"
  private val MiddleName = "Wan"
  private val LastName = "Kenobi"


  private val bytes: Array[Byte] = {
    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user.avsc"))
    val user = new GenericData.Record(schemaForUser)
    user.put("first_name", FirstName)
    user.put("middle_name", MiddleName)
    user.put("last_name", LastName)
    user.serToBytes(Some(schemaForUser))
  }

  def generateContent(): Tables.FlowDataContentRow =
    Tables.FlowDataContentRow(UUID.randomUUID().toString,
      Some(-1),
      Some(Timestamp.from(Instant.now())),
      Some(bytes))

  def createContent(ira: IntermediateResultsAdapter): Future[Unit] = {
    ira.createContent(generateContent())
  }

  def generateProvenance(componentId: String,
                         contentClaimId: String): FlowDataProvenance = {
    val now = Date.from(Instant.now()).getTime.toDouble
    FlowDataProvenance(UUID.randomUUID().toString,
      1,
      now,
      now,
      now,
      bytes.size,
      0,
      1,
      "CONTENT_MODIFIED",
      RemoteProcessor.SchemaIdKey + ":user",
      "",
      "",
      componentId,
      "PROCESSOR",
      "",
      "",
      UUID.randomUUID().toString,
      "",
      "",
      "",
      "",
      "success",
      UUID.randomUUID().toString,
      contentClaimId,
      UUID.randomUUID().toString)
  }


  def createContentProvenance(ira: IntermediateResultsAdapter): Future[Assertion] = {
    val fdc1 = generateContent()
    Thread.sleep(1000)
    val fdc2 = generateContent()
    Thread.sleep(1000)
    val fdc3 = generateContent()
    Thread.sleep(1000)
    val fdc4 = generateContent()

    val componentId1 = UUID.randomUUID().toString
    val fdp1 = generateProvenance(componentId1, fdc1.id)
    val fdp2 = generateProvenance(componentId1, fdc2.id)
    val fdp3 = generateProvenance(componentId1, fdc3.id)


    val componentId2 = UUID.randomUUID().toString
    val fdp4 = generateProvenance(componentId2, fdc4.id)

    ira.createContent(fdc1).
      flatMap(unit => ira.createProvenance(fdp1)).
      flatMap(unit => ira.createContent(fdc2)).
      flatMap(unit => ira.createProvenance(fdp2)).
      flatMap(unit => ira.createContent(fdc3)).
      flatMap(unit => ira.createProvenance(fdp3)).
      flatMap(unit => ira.createContent(fdc4)).
      flatMap(unit => ira.createProvenance(fdp4)).
      flatMap(unit => ira.getProvenanceByComponentId(componentId1, 2)).
      flatMap(provs => {
        assert(provs.size == 2)
        assert(provs.head.timestamp.getTime >  provs.tail.head.timestamp.getTime)
      }).
      flatMap(unit => ira.deleteProvenanceByComponentId(componentId1)).
      flatMap(res => assert(res == 3)).
      flatMap(as => ira.getProvenanceByComponentId(componentId2, 2)).
      flatMap(provs => {
        assert(provs.size == 1)
        assert(provs.head.id == fdc4.id)
      })
  }

  def incDecContentClaimCount(ira: IntermediateResultsAdapter): Future[Assertion] = {
    val fdc = generateContent()
    ira.createContent(fdc).

      flatMap(unit => ira.getClaimantCount(fdc.id)).
      flatMap(c => assert(c.get == -1)).
      flatMap(as => ira.incrementClaimaintCount(fdc.id)).

      flatMap(res => assert(res.get == 0)).
      flatMap(as => ira.getClaimantCount(fdc.id)).
      flatMap(c => assert(c.get == 0)).
      flatMap(as => ira.incrementClaimaintCount(fdc.id)).

      flatMap(res => assert(res.get == 1)).
      flatMap(as => ira.getClaimantCount(fdc.id)).
      flatMap(c => assert(c.get == 1)).
      flatMap(as => ira.decrementClaimaintCount(fdc.id)).

      flatMap(res => assert(res.get == 0)).
      flatMap(as => ira.getClaimantCount(fdc.id)).
      flatMap(c => assert(c.get == 0)).
      flatMap(as => ira.decrementClaimaintCount(fdc.id)).

      flatMap(res => assert(res.get == -1)).
      flatMap(as => ira.getClaimantCount(fdc.id)).
      flatMap(c => assert(c.get == -1))
  }

  def deleteContent(ira: IntermediateResultsAdapter): Future[Assertion] = {
    val fdc = generateContent()
    ira.createContent(fdc).
      flatMap(unit => ira.deleteContent(fdc.id)).
      flatMap(res => assert(res == 1)).
      flatMap(as => ira.getContent(fdc.id)).
      flatMap(c => assert(c.isEmpty))
  }

  def purgeContentProvenance(ira: IntermediateResultsAdapter): Future[Assertion] = {
    val fdc1 = generateContent()
    val fdc2 = generateContent()
    val fdc3 = generateContent()
    val fdc4 = generateContent()

    val componentId1 = UUID.randomUUID().toString
    val fdp1 = generateProvenance(componentId1, fdc1.id)
    val fdp2 = generateProvenance(componentId1, fdc2.id)
    val fdp3 = generateProvenance(componentId1, fdc3.id)

    ira.createContent(fdc1).
      flatMap(unit => ira.createProvenance(fdp1)).
      flatMap(unit => ira.createContent(fdc2)).
      flatMap(unit => ira.createProvenance(fdp2)).
      flatMap(unit => ira.createContent(fdc3)).
      flatMap(unit => ira.createProvenance(fdp3)).
      flatMap(unit => ira.getContentSize).
      flatMap(size => assert(size == 3)).
      flatMap(as => ira.getProvenanceSize).
      flatMap(size => assert(size == 3)).
      flatMap(res => ira.purgeContent()).
      flatMap(res => assert(res == 3)).
      flatMap(as => ira.getContentSize).
      flatMap(size => assert(size == 0)).
      flatMap(as => ira.purgeProvenance()).
      flatMap(res => assert(res == 3)).
      flatMap(as => ira.getProvenanceSize).
      flatMap(size => assert(size == 0))
  }

  def queryProvenanceEvents(ira: IntermediateResultsAdapter) = {
    val fdc1 = generateContent()
    Thread.sleep(1000)
    val fdc2 = generateContent()
    Thread.sleep(1000)
    val fdc3 = generateContent()
    Thread.sleep(1000)
    val fdc4 = generateContent()

    val componentId1 = UUID.randomUUID().toString
    val fdp1 = generateProvenance(componentId1, fdc1.id)
    val fdp2 = generateProvenance(componentId1, fdc2.id)
    val fdp3 = generateProvenance(componentId1, fdc3.id)


    val componentId2 = UUID.randomUUID().toString
    val fdp4 = generateProvenance(componentId2, fdc4.id)

    ira.createContent(fdc1).
      flatMap(unit => ira.createProvenance(fdp1)).
      flatMap(unit => ira.createContent(fdc2)).
      flatMap(unit => ira.createProvenance(fdp2)).
      flatMap(unit => ira.createContent(fdc3)).
      flatMap(unit => ira.createProvenance(fdp3)).
      flatMap(unit => ira.createContent(fdc4)).
      flatMap(unit => ira.createProvenance(fdp4)).
      flatMap(unit => ira.getProvenanceEventById(fdp1.id)).
      flatMap(prov => ira.getProvenanceEventsByEventId(prov.get.eventId, 3)).
      flatMap(provs => {
        assert(provs.size == 3)
        val eventIds = provs.map(_.eventId)
        val minEventId = eventIds.min

        assert(provs.exists(fdp => fdp.eventId == minEventId))
        assert(provs.exists(fdp => fdp.eventId == minEventId + 1))
        assert(provs.exists(fdp => fdp.eventId == minEventId + 2))
        Future(minEventId)
      }).
      flatMap(minEventId => ira.getProvenanceEventByEventId(minEventId + 1)).
      flatMap(prov => {
        assert(prov.isDefined)
        assert(prov.get.id == fdp2.id)
      }).
      flatMap(as => ira.getProvenanceEventsByEventType("CONTENT_MODIFIED")).
      flatMap(provs => {
        assert(provs.size == 4)
      }).
      flatMap(as => ira.getProvenanceEventsByFlowFileUuid(fdp1.flowFileUuid)).
      flatMap(provs => assert(provs.size == 1)).
      flatMap(as => ira.getProvenanceEventsByComponentId(componentId1)).
      flatMap(provs => assert(provs.size == 3)).
      flatMap(as => ira.getProvenanceEventsByRelationship("success")).
      flatMap(provs => assert(provs.size == 4))
  }


}
