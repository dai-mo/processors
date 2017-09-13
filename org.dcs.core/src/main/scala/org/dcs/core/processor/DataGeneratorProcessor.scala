package org.dcs.core.processor

import java.util

import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor.RelationshipType.Success
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse
import org.dcs.commons.serde.DataGenerator

object DataGeneratorProcessor {

  val NbOfRecordsKey = "nb-of-records"
  val NbOfRecordsProperty = RemoteProperty("Number of records to generate",
    NbOfRecordsKey,
    "Number of records to generate",
    defaultValue = "100",
    required = true,
    `type` = PropertyType.Int)

  def apply(): DataGeneratorProcessor = new DataGeneratorProcessor

}

class DataGeneratorProcessor extends Ingestion {
  import DataGeneratorProcessor._

  override def execute(record: Option[GenericRecord], properties: util.Map[String, String]): List[Either[ErrorResponse, (String, AnyRef)]] = {
    DataGenerator.persons(propertyValue(NbOfRecordsProperty, properties).toInt)
      .map(p => Right(Success.id, p))
  }

  override def _relationships(): Set[RemoteRelationship] = {
    Set(Success)
  }


  override def metadata(): MetaData =
    MetaData(description =  "Data Generator",
      tags = List("generator", "prototype"))

  override def _properties():List[RemoteProperty] = List(NbOfRecordsProperty)


  override def schemaId: String = DataGenerator.PersonSchemaId

}
