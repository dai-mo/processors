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

import java.util.UUID

import org.apache.avro.Schema
import org.dcs.api.processor.{CoreProperties, ExternalProcessorProperties}
import org.dcs.core.CoreUnitFlatSpec
import org.dcs.core.processor.KaaIngestionProcessor
import org.dcs.iot.kaa.KaaIoTClient

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  *
  * @author cmathew
  * @constructor
  */
class KaaIngestionProcessorSpec extends CoreUnitFlatSpec {

  "Kaa Ingestion Processor Lifecycle" should "be valid" taggedAs IT in {
    val kaaIoTClient = KaaIoTClient()
    val processor = KaaIngestionProcessor()
    val properties = processor.properties()

    val kaaApplicationsProperty = properties.asScala.find(_.name == KaaIngestionProcessor.KaaApplicationKey)
    assert(kaaApplicationsProperty.isDefined)
    assert(kaaApplicationsProperty.get.possibleValues.size == 3)

    val heartbeatMonitorApp = kaaApplicationsProperty.get.possibleValues.asScala.find(_.displayName == "Heartbeat Monitor")
    assert(heartbeatMonitorApp.isDefined)

    val rootInputPortId = UUID.randomUUID().toString
    val propertyValues = Map(KaaIngestionProcessor.KaaApplicationKey -> heartbeatMonitorApp.get.value,
      ExternalProcessorProperties.RootInputPortIdKey -> rootInputPortId).asJava

    // Test Read schema retrieval
    val applicationLogSchema = processor.resolveProperties(propertyValues).get(CoreProperties.ReadSchemaKey)
    val parsedSchema = new Schema.Parser().parse(applicationLogSchema)

    assert(parsedSchema.getNamespace == "org.dcs.iot.kaa.schema.log")
    assert(parsedSchema.getFields.size == 1)
    assert(parsedSchema.getField("heartbeat").name() == "heartbeat")

    // Test global start
    processor.preStart(propertyValues)
    val createdLogAppender = Await.result(kaaIoTClient.logAppenderWithRootInputPortId(heartbeatMonitorApp.get.value, rootInputPortId),
      10 seconds)
    assert(createdLogAppender.isDefined)
    // Test global stop
    processor.preStop(propertyValues)
    val deletedLogAppender = Await.result(kaaIoTClient.logAppenderWithRootInputPortId(heartbeatMonitorApp.get.value, rootInputPortId),
      10 seconds)
    assert(deletedLogAppender.isEmpty)


  }

}
