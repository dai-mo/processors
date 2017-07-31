package org.dcs.core.processor

import java.util

import org.apache.avro.data.Json
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.api.processor.RelationshipType._
import org.dcs.api.processor._
import org.dcs.commons.error.{ErrorConstants, ErrorResponse, HttpErrorResponse}
import org.dcs.commons.ws.{ApiConfig, JerseyRestClient}

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.commons.serde.JsonSerializerImplicits._

object GBIFOccurrenceProcessor {

  val ApiVersion = "v1"

  val SpeciesNamePropertyKey = "species-name"
  val SpeciesNameProperty = RemoteProperty(displayName = "Species Name",
    name = SpeciesNamePropertyKey,
    description =  "Species Name to search for",
    required = true)

  def apply(): GBIFOccurrenceProcessor = {
    new GBIFOccurrenceProcessor()
  }

}

/**
  * Created by cmathew on 09.11.16.
  */
class GBIFOccurrenceProcessor extends StatefulRemoteProcessor
  with JerseyRestClient
  with ApiConfig
  with Ingestion {

  import GBIFOccurrenceProcessor._

  val limit = 200
  var offset = 0
  var endOfRecords = false
  var noOfApiCalls = 0


  override def initState(): Unit = {
    offset = 0
    endOfRecords = false
  }

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, (String, GenericRecord)]] = {
    val species = propertyValue(SpeciesNameProperty, propertyValues)

    val gbifResponse =  Await.result(
      getAsEither(path = "/search",
        queryParams = List(
          ("scientificName", species),
          ("offset", offset.toString),
          ("limit", limit.toString))

      ), 20.seconds)

    if(gbifResponse.isLeft)
      List(Left(ErrorResponse(gbifResponse.left.get.code, gbifResponse.left.get.message, gbifResponse.left.get.description)))
    else
    {
      val response = gbifResponse.right.get.readEntity(classOf[String])
      val json: util.LinkedHashMap[String, AnyRef] =
        Json.parseJson(response)
          .asInstanceOf[util.LinkedHashMap[String, AnyRef]]

      offset = json.get("offset").asInstanceOf[Int]
      endOfRecords = json.get("endOfRecords").asInstanceOf[Boolean]
      val count = json.get("count").asInstanceOf[Int]

      noOfApiCalls = noOfApiCalls + 1


      if (count > 200000)
        List(Left(ErrorResponse(ErrorConstants.GlobalFlowErrorCode, "Invalid Request", "Occurrence record count exceeds download limit (200000)")))
      else if (endOfRecords || noOfApiCalls > 1)
        List()
      else
        json.get("results").asInstanceOf[util.List[util.LinkedHashMap[String, AnyRef]]].asScala
          .map(value => {
            val goJson = value.toJson
            val gbifOccurrence = value.toJson.toGenericRecord(AvroSchemaStore.get(schemaId))
            Right((Success.id, gbifOccurrence))
          }).toList
    }
  }


  override def _relationships(): Set[RemoteRelationship] = {
    Set(Success)
  }


  override def metadata(): MetaData =
    MetaData(description =  "GBIF Occurrence Processor",
      tags = List("GBIF", "Occurrence", "Species"))


  override def baseUrl(): String = "http://api.gbif.org/" + ApiVersion + "/occurrence"

  override def error(status: Int, message: String): HttpErrorResponse =
    ErrorResponse(ErrorConstants.GlobalFlowErrorCode, message).http(status)

  override def _properties(): List[RemoteProperty] = List(SpeciesNameProperty)

}
