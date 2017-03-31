package org.dcs.core.processor

/**
  * Created by cmathew on 18.11.16.
  */

import java.util

import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

import scala.collection.JavaConverters._

import org.dcs.api.processor.RelationshipType._

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
  with FieldsToMap{

  import LatLongValidationProcessor._

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, (String, GenericRecord)]] = {
    var invalid = false
    val m = record.mappings(propertyValues)
    val decimalLatitude  = m.get(LatitudeKey).asDouble
    val decimalLongitude = m.get(LongitudeKey).asDouble


    if(decimalLatitude.isEmpty || decimalLongitude.isEmpty)
      invalid = true
    else {
      if(decimalLatitude.get < -90 ||
        decimalLatitude.get > 90 ||
        decimalLongitude.get < -180 ||
        decimalLongitude.get > 180)
        invalid = true
    }
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

  def fields: List[String] = List(LatitudeKey, LongitudeKey)
}

