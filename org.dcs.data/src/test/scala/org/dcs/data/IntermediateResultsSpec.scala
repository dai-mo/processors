package org.dcs.data

import java.time.Instant
import java.util.{Date, UUID}

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.processor.RemoteProcessor
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore

/**
  * Created by cmathew on 02.02.17.
  */
class IntermediateResultsSpec extends IntermediateResultsBehaviour {
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

    IntermediateResults.purge()

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

    IntermediateResults.createContent(fdc1)
    IntermediateResults.createProvenance(fdp1)

    IntermediateResults.createContent(fdc2)
    IntermediateResults.createProvenance(fdp2)

    IntermediateResults.createContent(fdc3)
    IntermediateResults.createProvenance(fdp3)

    IntermediateResults.createContent(fdc4)
    IntermediateResults.createProvenance(fdp4)

    var provenanceQueryResults = IntermediateResults.listProvenanceByComponentId(componentId1, 2)
    assert(provenanceQueryResults.size == 2)
    assert(provenanceQueryResults.head.timestamp.getTime >  provenanceQueryResults.tail.head.timestamp.getTime)

    IntermediateResults.deleteProvenanceByComponentId(componentId1)
    provenanceQueryResults = IntermediateResults.listProvenanceByComponentId(componentId2, 1)
    assert(provenanceQueryResults.size == 1)
    assert(provenanceQueryResults.head.id == fdc4.id)

  }

}

trait IntermediateResultsBehaviour extends DataUnitSpec {

}
