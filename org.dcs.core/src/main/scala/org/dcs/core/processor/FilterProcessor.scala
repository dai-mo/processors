package org.dcs.core.processor

/**
  * Created by cmathew on 19.11.16.
  */


import java.util

import com.google.common.net.MediaType
import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

import scala.collection.JavaConverters._

object FilterProcessor {

  val FilterTermPropertyKey = "filter-term"
  val FilterTermProperty = RemoteProperty(displayName = "Term to filter",
    name = FilterTermPropertyKey,
    description =  "Term to filter",
    required = true)

  val FilterPropertyKey = "filter"
  val FilterProperty = RemoteProperty(displayName = "Filter to match",
    name = FilterPropertyKey,
    description =  "Filter to match",
    required = true)

  def apply(): FilterProcessor = {
    new FilterProcessor()
  }

}

/**
  * Created by cmathew on 09.11.16.
  */
class FilterProcessor extends RemoteProcessor {

  import FilterProcessor._

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, GenericRecord]] = {
    var invalid = true

    val term = propertyValue(FilterTermProperty, propertyValues)
    val filter = propertyValue(FilterProperty, propertyValues)

    val value  = record.get.get(term)

    if(value != null) {
      val valueAsString = value.toString
      if(valueAsString.contains(filter))
        invalid = false
    } else
      invalid = false

    if(invalid)
      List(Right(null))
    else
      List(Right(record.get))
  }


  override def relationships(): util.Set[RemoteRelationship] = {
    Set(RelationshipType.success, RelationshipType.failure).asJava
  }
  override def configuration: Configuration = {
    Configuration(inputMimeType = MediaType.OCTET_STREAM.toString,
      outputMimeType = MediaType.OCTET_STREAM.toString,
      processorClassName =  this.getClass.getName,
      inputRequirementType = InputRequirementType.InputRequired)
  }

  override def metadata(): MetaData =
    MetaData(description =  "Filter Processor",
      tags = List("filter").asJava)

  override def properties(): util.List[RemoteProperty] =
    List(FilterTermProperty, FilterProperty).asJava

  override def schemaId: String = null

  override def processorType(): String = RemoteProcessor.WorkerProcessorType
}

