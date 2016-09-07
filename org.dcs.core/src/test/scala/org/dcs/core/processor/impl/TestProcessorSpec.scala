package org.dcs.core.processor.impl

import org.dcs.core.BaseUnitSpec
import org.dcs.core.processor.TestProcessor

import scala.collection.JavaConverters._

class TestProcessorSpec extends BaseUnitSpec {

	"The Test Api Service" should "return correct response for valid input" in {
		val testProcessor = new TestProcessor();
		val user = "Bob";
		def userGreeting(user:String) = "Hello " + user
		def defaultGreeting() = "Hello World"
		assertResult(userGreeting(user)) {
			testProcessor.
				execute("Hello ".getBytes(), Map(TestProcessor.UserPropertyName -> user).asJava).
				response
		}
		assertResult(defaultGreeting()) {
			testProcessor.
				execute("Hello ".getBytes(), Map[String, String]().asJava).
				response
		}

		assertResult(defaultGreeting()) {
			testProcessor.
				execute("Hello ".getBytes(), null).
				response
		}


	}
}