package org.dcs.core.processor

/**
  * Created by cmathew on 18.11.16.
  */

import java.util

import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

import scala.collection.JavaConverters._

object LatLongValidationProcessor {

  val LatitudeKey = "decimalLatitude"
  val LongitudeKey = "decimalLongitude"

  def apply(): LatLongValidationProcessor = {
    new LatLongValidationProcessor()
  }

}

/**
  * Created by cmathew on 09.11.16.
  */
class LatLongValidationProcessor extends RemoteProcessor
  with Worker {

  import LatLongValidationProcessor._

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, GenericRecord]] = {
    var invalid = false
    val decimalLatitude  = record.getAsDouble(LatitudeKey)
    val decimalLongitude = record.getAsDouble(LongitudeKey)


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
      List(Right(null))
    else
      List(Right(record.get))
  }


  override def _relationships(): Set[RemoteRelationship] = {
    val invalid = RemoteRelationship("INVALID_LAT_LONG",
      "All status updates will be routed to this relationship")
    Set(RelationshipType.SUCCESS, RelationshipType.FAILURE)
  }

  override def metadata(): MetaData =
    MetaData(description =  "Lat/Long Validation Processor",
      tags = List("latitude", "longitude", "validation").asJava)

  override def _properties(): List[RemoteProperty] = Nil

  def fields: List[String] = List(LatitudeKey, LongitudeKey)
}

