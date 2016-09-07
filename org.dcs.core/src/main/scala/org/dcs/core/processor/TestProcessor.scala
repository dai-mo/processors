package org.dcs.core.processor

import java.util.{List => JavaList, Map => JavaMap, Set => JavaSet}
import javax.enterprise.inject.Default

import com.google.common.net.MediaType
import org.dcs.api.processor._
import org.dcs.api.service.TestResponse
import org.dcs.commons.JsonSerializerImplicits._
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

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

  override def execute(input: Array[Byte], values: JavaMap[String, String]): TestResponse = {
    TestResponse(new String(input) + propertyValue(UserProperty, values))
  }

	override def trigger(input: Array[Byte], properties: JavaMap[String, String]): Array[Byte] = {
    execute(input, properties).toJson.getBytes()
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
    Configuration(inputMimeType = MediaType.PLAIN_TEXT_UTF_8.`type`(),
      outputMimeType = MediaType.JSON_UTF_8.`type`(),
      processorClassName =  this.getClass.getName,
      inputRequirementType = InputRequirementType.InputRequired)
  }

  override def metadata(): MetaData = {
    MetaData(description =  "Greeting Processor",
      tags = List("Greeting").asJava)
  }
}
