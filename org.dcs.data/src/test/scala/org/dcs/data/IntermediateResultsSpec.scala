package org.dcs.data

import java.time.Instant
import java.util.{Date, UUID}

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.processor.RemoteProcessor
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.data.slick.SlickPostgresIntermediateResults
import org.scalatest.Ignore

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.collection.JavaConverters._

/**
  * Created by cmathew on 02.02.17.
  */
@Ignore
class SlickPostgresIntermediateResultsSpec extends SlickPostgresIntermediateResultsBehaviour {
  val FirstName = "Obi"
  val MiddleName = "Wan"
  val LastName = "Kenobi"


  val bytes: Array[Byte] = {
    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user.avsc"))
    val user = new GenericData.Record(schemaForUser)
    user.put("first_name", FirstName)
    user.put("middle_name", MiddleName)
    user.put("last_name", LastName)
    user.serToBytes(Some(schemaForUser))
  }

  def generateContent(): FlowDataContent =
    FlowDataContent(UUID.randomUUID().toString, -1, Date.from(Instant.now()), bytes)

  def generateProvenance(eventId: Double,
                         componentId: String,
                         contentClaimId: String): FlowDataProvenance = {
    val now = Date.from(Instant.now()).getTime.toDouble
    FlowDataProvenance(UUID.randomUUID().toString,
      eventId,
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

  "Intermediate Results" should "be created and retrieved consistently" in {

    AvroSchemaStore.add("user")

    DbMigration.migratePostgres()

    SlickPostgresIntermediateResults.purge()

    val fdc1 = generateContent()
    Thread.sleep(1000)
    val fdc2 = generateContent()
    Thread.sleep(1000)
    val fdc3 = generateContent()
    Thread.sleep(1000)
    val fdc4 = generateContent()

    val componentId1 = UUID.randomUUID().toString
    val fdp1 = generateProvenance(1, componentId1, fdc1.id)
    val fdp2 = generateProvenance(2, componentId1, fdc2.id)
    val fdp3 = generateProvenance(3, componentId1, fdc3.id)


    val componentId2 = UUID.randomUUID().toString
    val fdp4 = generateProvenance(4, componentId2, fdc4.id)

    Await.result(SlickPostgresIntermediateResults.createContent(fdc1), Duration.Inf)
    Await.result(SlickPostgresIntermediateResults.createProvenance(fdp1), Duration.Inf)

    Await.result(SlickPostgresIntermediateResults.createContent(fdc2), Duration.Inf)
    Await.result(SlickPostgresIntermediateResults.createProvenance(fdp2), Duration.Inf)

    Await.result(SlickPostgresIntermediateResults.createContent(fdc3), Duration.Inf)
    Await.result(SlickPostgresIntermediateResults.createProvenance(fdp3), Duration.Inf)

    Await.result(SlickPostgresIntermediateResults.createContent(fdc4), Duration.Inf)
    Await.result(SlickPostgresIntermediateResults.createProvenance(fdp4), Duration.Inf)

    var provenanceQueryResults = Await.result(SlickPostgresIntermediateResults.listProvenanceByComponentId(componentId1, 2), Duration.Inf).asScala
    assert(provenanceQueryResults.size == 2)
    assert(provenanceQueryResults.head.timestamp.getTime >  provenanceQueryResults.tail.head.timestamp.getTime)

    Await.result(SlickPostgresIntermediateResults.deleteProvenanceByComponentId(componentId1), Duration.Inf)
    provenanceQueryResults = Await.result(SlickPostgresIntermediateResults.listProvenanceByComponentId(componentId2, 1), Duration.Inf).asScala
    assert(provenanceQueryResults.size == 1)
    assert(provenanceQueryResults.head.id == fdc4.id)

  }

}

trait SlickPostgresIntermediateResultsBehaviour extends DataUnitSpec {

}
