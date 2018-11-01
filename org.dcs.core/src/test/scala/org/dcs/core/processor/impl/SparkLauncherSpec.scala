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

import org.dcs.api.processor._
import org.dcs.commons.serde.JsonSerializerImplicits._
import org.dcs.commons.serde.{DataGenerator, JsonPath}
import org.dcs.core.CoreUnitFlatSpec
import org.dcs.core.processor.SparkBasicStatsProcessor
import org.scalatest.Ignore

import scala.collection.JavaConverters._

@Ignore
class SparkLauncherSpec extends CoreUnitFlatSpec {

  // NOTE: The SPARK_HOME environmental variable must be set here for this to work
  "Spark Basic Statistics Processor" should "launch correctly" in {

    val processor = SparkBasicStatsProcessor()
    processor.initState()

    val receiverProperty = RemoteProperty(ExternalProcessorProperties.ReceiverKey, ExternalProcessorProperties.ReceiverKey, "")
    val senderProperty = RemoteProperty(ExternalProcessorProperties.SenderKey, ExternalProcessorProperties.SenderKey, "")
    val fieldsToMapProperty = CoreProperties.fieldsToMapProperty(SparkBasicStatsProcessor().fields)
    val readSchemaIdProperty = CoreProperties.readSchemaIdProperty()

    val propertyValues = Map(
      receiverProperty -> "org.dcs.spark.receiver.TestReceiver?delay=1000&nbOfRecords=100",
      senderProperty -> "org.dcs.spark.sender.TestSender",
      readSchemaIdProperty -> DataGenerator.PersonSchemaId,
      fieldsToMapProperty -> Set(ProcessorSchemaField("average", PropertyType.Double, JsonPath.Root + JsonPath.Sep + "age")).toJson
    ).asJava

    processor.onSchedule(propertyValues)

    Thread.sleep(20000)
    processor.onRemove(propertyValues)

  }
}
