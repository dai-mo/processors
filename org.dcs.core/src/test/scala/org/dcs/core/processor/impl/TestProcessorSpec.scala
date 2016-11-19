package org.dcs.core.processor.impl

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitSpec
import org.dcs.core.processor.TestProcessor

import scala.collection.JavaConverters._

class TestProcessorSpec extends CoreUnitSpec {

	"The Test Api Service" should "return correct response for valid input" in {
		val testProcessor = new TestProcessor()
		val user = "Bob"
		def userGreeting(user:String) = "Hello " + user
		def defaultGreeting() = "Hello World"

		val record = Some(new GenericData.Record(AvroSchemaStore.get("org.dcs.core.processor.TestRequestProcessor").get))
		record.get.put("request", "Hello ")
		assertResult(userGreeting(user)) {
			testProcessor.
				execute(record, Map(TestProcessor.UserPropertyName -> user).asJava)
				.head.right.get.get("response").asInstanceOf[String]
		}
		assertResult(defaultGreeting()) {
			testProcessor.
				execute(record, Map[String, String]().asJava)
				.head.right.get.get("response").asInstanceOf[String]
		}

		assertResult(defaultGreeting()) {
			testProcessor.
				execute(record, null)
				.head.right.get.get("response").asInstanceOf[String]
		}
	}
}