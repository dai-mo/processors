package org.dcs.core.processor

import java.util.{List => JavaList, Map => JavaMap, Set => JavaSet}

import com.google.common.net.MediaType
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.api.processor._
import org.dcs.api.service.TestResponse
import org.dcs.commons.error.ErrorResponse
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.commons.serde.JsonSerializerImplicits._

import scala.collection.JavaConverters._


object TestProcessor {
  val UserPropertyName = "user"
  val UserProperty = RemoteProperty(displayName = "User",
    name = UserPropertyName,
    description =  "User To Greet",
    defaultValue =  "World",
    possibleValues = Set(
      PossibleValue("World", "User World", "User Name of World"),
      PossibleValue("Bob", "User Bob", "User Name of Bob"),
      PossibleValue("Bri", "User Bri", "User Name of Bri")).asJava,
    required = true)

  def apply(): TestProcessor = {
    new TestProcessor()
  }
}


class TestProcessor extends RemoteProcessor  {

  import org.dcs.core.processor.TestProcessor._

  override def execute(record: Option[GenericRecord], values: JavaMap[String, String]): List[Either[ErrorResponse, GenericRecord]] = {
    val testResponse = new GenericData.Record(AvroSchemaStore.get(schemaId).get)
    testResponse.put("response", record.get.get("request").toString + propertyValue(UserProperty, values))
    List(Right(testResponse))
  }


  override def properties(): JavaList[RemoteProperty] = {
    List(UserProperty).asJava
  }

  override def relationships(): JavaSet[RemoteRelationship] = {
    val success = RemoteRelationship(RelationshipType.SucessRelationship,
      "All status updates will be routed to this relationship")
    Set(success).asJava
  }

  override def configuration: Configuration = {
    Configuration(inputMimeType = MediaType.OCTET_STREAM.toString,
      outputMimeType = MediaType.OCTET_STREAM.toString,
      processorClassName =  this.getClass.getName,
      inputRequirementType = InputRequirementType.InputRequired)
  }

  override def metadata(): MetaData = {
    MetaData(description =  "Greeting Processor",
      tags = List("Greeting").asJava)
  }

  override def schemaId: String = "org.dcs.core.processor.TestResponseProcessor"

  override def processorType(): String = RemoteProcessor.WorkerProcessorType
}
