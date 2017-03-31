package org.dcs.core.processor

/**
  * Created by cmathew on 20.11.16.
  */

import java.io.{File, FileWriter}
import java.util

import com.opencsv.CSVWriter
import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

import scala.collection.JavaConverters._
import org.dcs.api.processor.RelationshipType._

object CSVFileOutputProcessor {

  val FileNamePropertyKey = "file-name"
  val FileNameProperty = RemoteProperty(displayName = "Output File Name",
    name = FileNamePropertyKey,
    description =  "Output File Name",
    required = true)

  val FileBaseUrlPropertyKey = "file-base-url"
  val FileBaseUrlProperty = RemoteProperty(displayName = "Output File Base Url",
    name = FileBaseUrlPropertyKey,
    description =  "Output File Base Url",
    defaultValue = "",
    required = true)

  def apply(): CSVFileOutputProcessor = {
    new CSVFileOutputProcessor()
  }

}

/**
  * Created by cmathew on 09.11.16.
  */
class CSVFileOutputProcessor extends StatefulRemoteProcessor
  with Sink {

  import CSVFileOutputProcessor._

  var writer: CSVWriter = null

  var headers: List[String] = List()

  override def initState(): Unit = {

  }

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, (String, GenericRecord)]] = {
      var fileBaseUrl = propertyValue(FileBaseUrlProperty, propertyValues)
      if (!fileBaseUrl.isEmpty)
        fileBaseUrl = fileBaseUrl + File.separator

      val fileName = propertyValue(FileNameProperty, propertyValues)

      if (writer == null) {
        writer = new CSVWriter(new FileWriter(fileBaseUrl + fileName + ".csv"))
        headers = record.get.getSchema.getFields.asScala.map(field => field.name()).toList
        writer.writeNext(headers.toArray)
      }

      if (writer != null) {
        val row = headers.indices.toList.map(key => Option(record.get.get(key)).getOrElse("").toString)
        writer.writeNext(row.toArray)
        writer.flush()
      }


    List(Right((Success.id, record.get)))
  }


  override def _relationships(): Set[RemoteRelationship] = {
    Set(Success)
  }


  override def metadata(): MetaData =
    MetaData(description =  "CSV File Output",
      tags = List("csv", "file", "writer"))

  override def _properties():List[RemoteProperty] = List(FileNameProperty)

  override def onShutdown(properties: util.List[RemoteProperty]): Boolean = {
    if(writer != null)
      writer.close()
    true
  }

  override def onRemove(properties: util.List[RemoteProperty]): Boolean = {
    if(writer != null)
      writer.close()
    true
  }

}

