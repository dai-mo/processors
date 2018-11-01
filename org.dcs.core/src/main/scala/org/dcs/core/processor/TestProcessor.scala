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
