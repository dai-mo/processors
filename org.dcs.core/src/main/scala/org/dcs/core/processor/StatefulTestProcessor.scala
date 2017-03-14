package org.dcs.core.processor

import java.util.{UUID, Map => JavaMap }

import com.google.common.net.MediaType
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse
import org.dcs.commons.serde.AvroSchemaStore

import scala.collection.JavaConverters._


object StatefulTestProcessor {
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

  def apply(): StatefulTestProcessor = {
    new StatefulTestProcessor()
  }
}


class StatefulTestProcessor extends StatefulRemoteProcessor
  with Worker {

  import StatefulTestProcessor._

  var suffix: String = ""

	override def _properties(): List[RemoteProperty] = {
		List(UserProperty)
	}

  override def _relationships(): Set[RemoteRelationship] = {
    val success = RemoteRelationship(RelationshipType.SucessRelationship,
      "All status updates will be routed to this relationship")
    Set(success)
  }

  override def execute(record: Option[GenericRecord], values: JavaMap[String, String]): List[Either[ErrorResponse, GenericRecord]] = {
    val testResponse = new GenericData.Record(AvroSchemaStore.get(schemaId).get)
    testResponse.put("response", "id : " + suffix + record.get.get("request").toString + propertyValue(UserProperty, values))
    List(Right(testResponse))
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

  override def initState(): Unit = {
    suffix = UUID.randomUUID().toString
  }

  override def className: String = this.getClass.getName
}
