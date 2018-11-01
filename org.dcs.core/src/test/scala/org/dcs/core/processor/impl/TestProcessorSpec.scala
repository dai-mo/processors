/*
 * Copyright (c) 2017-2018 brewlabs SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dcs.core.processor.impl

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitFlatSpec
import org.dcs.core.processor.TestProcessor

import scala.collection.JavaConverters._
import org.dcs.api.processor.CoreProperties._

class TestProcessorSpec extends CoreUnitFlatSpec {

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
				.head.right.get._2.get("response").asInstanceOf[String]
		}
		assertResult(defaultGreeting()) {
			testProcessor.
				execute(record, Map(ReadSchemaIdKey -> testRequestSchemaId).asJava)
				.head.right.get._2.get("response").asInstanceOf[String]
		}

		assertResult(defaultGreeting()) {
			testProcessor.
				execute(record, Map(ReadSchemaIdKey -> testRequestSchemaId).asJava)
				.head.right.get._2.get("response").asInstanceOf[String]
		}
	}
}