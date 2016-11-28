package org.dcs.core.processor

/**
  * Created by cmathew on 18.11.16.
  */

import java.util

import com.google.common.net.MediaType
import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

import scala.collection.JavaConverters._

object LatLongValidationProcessor {

  def apply(): LatLongValidationProcessor = {
    new LatLongValidationProcessor()
  }

}

/**
  * Created by cmathew on 09.11.16.
  */
class LatLongValidationProcessor extends RemoteProcessor {

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, GenericRecord]] = {
    var invalid = false
    val lat  = record.get.get("decimalLatitude")
    val long = record.get.get("decimalLongitude")

    if(lat == null || long == null)
      invalid = true
    else {
      val decimalLatitude = record.get.get("decimalLatitude").asInstanceOf[Double]
      val decimalLongitude = record.get.get("decimalLongitude").asInstanceOf[Double]

      if(decimalLatitude < -90 ||
        decimalLatitude > 90 ||
        decimalLongitude < -180 ||
        decimalLongitude > 180)
        invalid = true
    }
    if(invalid)
      List(Right(null))
    else
      List(Right(record.get))
  }


  override def relationships(): util.Set[RemoteRelationship] = {
    val invalid = RemoteRelationship("INVALID_LAT_LONG",
      "All status updates will be routed to this relationship")
    Set(RelationshipType.success, RelationshipType.failure).asJava
  }
  override def configuration: Configuration = {
    Configuration(inputMimeType = MediaType.OCTET_STREAM.toString,
      outputMimeType = MediaType.OCTET_STREAM.toString,
      processorClassName =  this.getClass.getName,
      inputRequirementType = InputRequirementType.InputRequired)
  }

  override def metadata(): MetaData =
    MetaData(description =  "Lat/Long Validation Processor",
      tags = List("latitude", "longitude", "validation").asJava)

  override def properties(): util.List[RemoteProperty] = new util.ArrayList[RemoteProperty]()

  override def schemaId: String = null

  override def processorType(): String = RemoteProcessor.WorkerProcessorType
}

