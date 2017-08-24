package org.dcs.core.processor.impl

import org.dcs.api.processor.{CoreProperties, RemoteProperty}
import org.dcs.core.processor.SparkBasicStatsProcessor
import org.dcs.spark.processor.SparkUnitFlatSpec
import org.dcs.spark.receiver.TestReceiver
import org.dcs.commons.serde.JsonSerializerImplicits._
import scala.collection.JavaConverters._

//@Ignore
class SparkLauncherSpec extends SparkUnitFlatSpec {

  // NOTE: The SPARK_HOME environmental variable must be set here for this to work
  "Spark Basic Statistics Processor" should "launch correctly" in {

    val processor = SparkBasicStatsProcessor()
    processor.initState()

    val receiverProperty = RemoteProperty(CoreProperties.ReceiverKey, CoreProperties.ReceiverKey, "")
    val senderProperty = RemoteProperty(CoreProperties.SenderKey, CoreProperties.SenderKey, "")
    val fieldsToMapProperty = CoreProperties.fieldsToMapProperty(SparkBasicStatsProcessor().fields)
    val readSchemaIdProperty = CoreProperties.readSchemaIdProperty()

    val propertyValues = Map(
      receiverProperty -> "org.dcs.spark.receiver.TestReceiver",
      senderProperty -> "org.dcs.spark.sender.TestSender",
      readSchemaIdProperty -> TestReceiver.PersonSchemaId,
      fieldsToMapProperty -> TestReceiver.FieldsToMap.toJson
    ).asJava

    processor.onSchedule(propertyValues)

    Thread.sleep(20000)
    processor.onRemove(propertyValues)

  }
}
