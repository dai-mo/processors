package org.dcs.core.processor

import java.nio.charset.StandardCharsets
import java.util
import java.util.UUID

import org.dcs.api.processor._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.commons.serde.DataGenerator.PersonSchemaId
import org.dcs.iot.kaa.{KaaClientConfig, KaaIoTClient, NifiS2SConfig}

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.dcs.commons.serde.JsonSerializerImplicits._

import scala.concurrent.ExecutionContext.Implicits._

object KaaIngestionProcessor {
  val KaaApplicationKey = "kaa-application"
  val KaaApplicationProperty = RemoteProperty("Kaa Application",
    KaaApplicationKey,
    "Kaa Application to ingest data from",
    required = true
  )

  def apply() = new KaaIngestionProcessor()
}

class KaaIngestionProcessor extends InputPortIngestion {
  import KaaIngestionProcessor._


  override def metadata(): MetaData =
    MetaData(description =  "Kaa Ingestion Processor",
      tags = List("kaa", "iot"))

  override def _properties(): List[RemoteProperty] = {

    val kaaIoTClient = KaaIoTClient()
    val credentials = KaaIoTClient.credentials

    val applications = Await.result(kaaIoTClient.applications(), 10 seconds)
    val pvs = applications.map(app => PossibleValue(app.applicationToken, app.name, app.name))
    KaaApplicationProperty.setPossibleValuesWithDefault(pvs.toSet)
    List(KaaApplicationProperty)
  }

  override def preStart(propertyValues: util.Map[String, String]): Boolean = {
    val rootInputPortId = propertyValues.get(ExternalProcessorProperties.RootInputPortIdKey)
    val applicationToken = propertyValues.get(KaaApplicationKey)
    val kaaIoTClient = KaaIoTClient()


    Await.result(
      kaaIoTClient.application(applicationToken)
        .flatMap(app => kaaIoTClient.createLogAppender(app,
          "Nifi S2S Appender",
          "org.dcs.iot.kaa.NifiS2SAppender",
          "Nifi S2S Appender",
          NifiS2SConfig(rootInputPortId).toJson))
        .map(response => true),
      10 seconds)
  }

  override def preStop(propertyValues: util.Map[String, String]): Boolean = {
    val rootInputPortId = propertyValues.get(ExternalProcessorProperties.RootInputPortIdKey)
    val applicationToken = propertyValues.get(KaaApplicationKey)
    val kaaIoTClient = KaaIoTClient()

    Await.result(kaaIoTClient.removeLogAppenderWithRootInputPortId(applicationToken, rootInputPortId),
      10 seconds)
  }

  override def _resolveProperties(propertyValues: Map[String, String]): Map[String, String] = {
    Map(CoreProperties.ReadSchemaKey ->
      Option(propertyValues(KaaApplicationKey))
        .map { applicationToken =>
          val kaaIoTClient = KaaIoTClient()
          Await.result(kaaIoTClient.maxApplicationLogSchema(applicationToken), 10 seconds)
        }
        .getOrElse(""))
  }

}
