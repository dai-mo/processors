package org.dcs.core.processor.impl

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitSpec
import org.dcs.core.processor.TestProcessor

import scala.collection.JavaConverters._
import org.dcs.api.processor.CoreProperties._

class TestProcessorSpec extends CoreUnitSpec {

	"The Test Api Service" should "return correct response for valid input" in {
    val testRequestSchemaId = "org.dcs.core.processor.TestRequest"
    val testResponseSchemaId = "org.dcs.core.processor.TestResponse"

		AvroSchemaStore.add(testRequestSchemaId)
    AvroSchemaStore.add(testResponseSchemaId)

		val testProcessor = new TestProcessor()
		val user = "Bob"
		def userGreeting(user:String) = "Hello " + user
		def defaultGreeting() = "Hello World"

		val record = Some(new GenericData.Record(AvroSchemaStore.get(testRequestSchemaId).get))
		record.get.put("request", "Hello ")
		assertResult(userGreeting(user)) {
			testProcessor.
				execute(record,
          Map(TestProcessor.UserPropertyName -> user, ReadSchemaIdKey -> testRequestSchemaId).asJava)
				.head.right.get.get("response").asInstanceOf[String]
		}
		assertResult(defaultGreeting()) {
			testProcessor.
				execute(record, Map(ReadSchemaIdKey -> testRequestSchemaId).asJava)
				.head.right.get.get("response").asInstanceOf[String]
		}

		assertResult(defaultGreeting()) {
			testProcessor.
				execute(record, Map(ReadSchemaIdKey -> testRequestSchemaId).asJava)
				.head.right.get.get("response").asInstanceOf[String]
		}
	}
}