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
