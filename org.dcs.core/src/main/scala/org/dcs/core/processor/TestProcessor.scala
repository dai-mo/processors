package org.dcs.core.processor

import java.util.{Map => JavaMap}

import com.google.common.net.MediaType
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse
import org.dcs.commons.serde.AvroSchemaStore

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


class TestProcessor extends RemoteProcessor
  with Worker {


  import org.dcs.core.processor.TestProcessor._

  AvroSchemaStore.add("org.dcs.core.processor.TestRequest")

  override def execute(record: Option[GenericRecord], values: JavaMap[String, String]): List[Either[ErrorResponse, (String, GenericRecord)]] = {
    val testResponse = new GenericData.Record(AvroSchemaStore.get(schemaId).get)
    testResponse.put("response", record.get.get("request").toString + propertyValue(UserProperty, values))
    List(Right((RelationshipType.Success.id, testResponse)))
  }


  override def _properties(): List[RemoteProperty] = {
    List(UserProperty)
  }

  override def _relationships(): Set[RemoteRelationship] = {
    Set(RelationshipType.Success)
  }

  override def configuration: Configuration = {
    Configuration(inputMimeType = MediaType.OCTET_STREAM.toString,
      outputMimeType = MediaType.OCTET_STREAM.toString,
      processorClassName =  this.getClass.getName,
      inputRequirementType = InputRequirementType.InputRequired)
  }

  override def metadata(): MetaData = {
    MetaData(description =  "Greeting Processor",
      tags = List("Greeting"))
  }

  override def schemaId: String = "org.dcs.core.processor.TestResponse"

}
