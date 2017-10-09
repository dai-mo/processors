package org.dcs.core.processor

import java.util

import scala.collection.JavaConverters._
import org.dcs.api.processor.{InputPortIngestion, MetaData, PossibleValue, RemoteProperty}
import org.dcs.commons.serde.DataGenerator

object KaaIngestionProcessor {
  val KaaApplicationKey = "kaa-application"
  val kaaApplicationProperty = RemoteProperty("Kaa Application",
    KaaApplicationKey,
    "Kaa Application to ingest data from",
    required = true,
    possibleValues = Set(PossibleValue("App1", "App1", "App1"), PossibleValue("App1", "App1", "App1")).asJava
  )

  def apply() = new KaaIngestionProcessor
}

class KaaIngestionProcessor extends InputPortIngestion {
  override def readSchema(properties: util.Map[String, String]): String = DataGenerator.PersonSchemaId

  override def metadata(): MetaData =
    MetaData(description =  "Kaa Ingestion Processor",
      tags = List("kaa", "iot"))
}
