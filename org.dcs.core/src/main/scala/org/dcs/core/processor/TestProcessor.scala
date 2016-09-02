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
  val UserProperty = PropertySettings("User", UserPropertyName, "User To Greet", "World")
}

@OsgiServiceProvider
@Properties(Array(
	new Property(name = "service.exported.interfaces", value = "org.dcs.api.processor.RemoteProcessor"),
	new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/processor/TestProcessor")
))
@Default
class TestProcessor extends RemoteProcessor {

  import org.dcs.core.processor.TestProcessor._

	override def properties(): JavaList[PropertySettings] = {
		List(UserProperty).asJava
	}

  override def relationships(): JavaSet[RelationshipSettings] = {
    val success = RelationshipSettings(RelationshipType.SucessRelationship,
      "All status updates will be routed to this relationship")
    Set(success).asJava
  }


  override def configuration(): Configuration = {
    Configuration(MediaType.PLAIN_TEXT_UTF_8.`type`(),
      MediaType.JSON_UTF_8.`type`())
  }

  override def schedule(): Boolean = true

  override def execute(input: Array[Byte], values: JavaMap[String, String]): TestResponse = {
    TestResponse(new String(input) + propertyValue(UserProperty, values))
  }

	override def trigger(input: Array[Byte], properties: JavaMap[String, String]): Array[Byte] = {
    execute(input, properties).toJson.getBytes()
	}

	override def unschedule(): Boolean =  true

	override def stop(): Boolean = true

	override def shutdown(): Boolean = true

	override def remove(): Boolean = true

}
