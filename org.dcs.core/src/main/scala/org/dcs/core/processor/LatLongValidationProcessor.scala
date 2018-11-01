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

/**
  * Created by cmathew on 18.11.16.
  */

import java.util

import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor.RelationshipType._
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

object LatLongValidationProcessor {

  val LatitudeKey = "latitude"
  val LongitudeKey = "longitude"

  def apply(): LatLongValidationProcessor = {
    new LatLongValidationProcessor()
  }
}

/**
  * Created by cmathew on 09.11.16.
  */
class LatLongValidationProcessor extends Worker
  with FieldsToMap {

  import LatLongValidationProcessor._

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, (String, GenericRecord)]] = {

    val m = record.mappings(propertyValues)
    val decimalLatitudes  = m.get(LatitudeKey).values[Double]
    val decimalLongitudes = m.get(LongitudeKey).values[Double]

    val invalid = decimalLatitudes.isEmpty ||
      decimalLatitudes.exists(dlat => dlat < -90 || dlat > 90) ||
      decimalLongitudes.isEmpty ||
      decimalLongitudes.exists(dlong => dlong < -180 || dlong > 180)

    if(invalid)
      List(Right((Invalid.id, record.get)))
    else
      List(Right((Valid.id, record.get)))
  }


  override def _relationships(): Set[RemoteRelationship] = {

    Set(Valid, Invalid)
  }

  override def metadata(): MetaData =
    MetaData(description =  "Lat/Long Validation Processor",
      tags = List("latitude", "longitude", "validation"))

  override def _properties(): List[RemoteProperty] = Nil

  def fields: Set[ProcessorSchemaField] = Set(ProcessorSchemaField(LatitudeKey, PropertyType.Double),
    ProcessorSchemaField(LongitudeKey, PropertyType.Double))
}

