package org.dcs.core.processor.impl

import org.dcs.api.processor.{CoreProperties, ProcessorSchemaField, PropertyType, RemoteProperty}
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

    val receiverProperty = RemoteProperty(CoreProperties.ReceiverKey, CoreProperties.ReceiverKey, "")
    val senderProperty = RemoteProperty(CoreProperties.SenderKey, CoreProperties.SenderKey, "")
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
