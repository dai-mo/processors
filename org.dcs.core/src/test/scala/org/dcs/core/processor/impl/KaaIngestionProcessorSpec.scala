package org.dcs.core.processor.impl

import java.util.UUID

import org.apache.avro.Schema
import org.dcs.api.processor.ExternalProcessorProperties
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
    assert(kaaApplicationsProperty.get.possibleValues.size == 2)

    val heartbeatMonitorApp = kaaApplicationsProperty.get.possibleValues.asScala.find(_.displayName == "Heartbeat Monitor")
    assert(heartbeatMonitorApp.isDefined)

    val rootInputPortId = UUID.randomUUID().toString
    val propertyValues = Map(KaaIngestionProcessor.KaaApplicationKey -> heartbeatMonitorApp.get.value,
      ExternalProcessorProperties.RootInputPortIdKey -> rootInputPortId).asJava

    // Test Read schema retrieval
    val applicationLogSchema = processor.readSchema(propertyValues)
    val parsedSchema = new Schema.Parser().parse(applicationLogSchema)

    assert(parsedSchema.getNamespace == "org.dcs.iot.kaa.schema.log")
    assert(parsedSchema.getFields.size == 1)
    assert(parsedSchema.getField("heartbeat").name() == "heartbeat")

    // Test global start
    processor.start(propertyValues)
    val createdLogAppender = Await.result(kaaIoTClient.logAppenderWithRootInputPortId(heartbeatMonitorApp.get.value, rootInputPortId),
      10 seconds)
    assert(createdLogAppender.isDefined)

    // Test global stop
    processor.stop(propertyValues)
    val deletedLogAppender = Await.result(kaaIoTClient.logAppenderWithRootInputPortId(heartbeatMonitorApp.get.value, rootInputPortId),
      10 seconds)
    assert(deletedLogAppender.isEmpty)


  }

}
