package org.dcs.core.processor

import java.util

import org.apache.avro.data.Json
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.api.processor._
import org.dcs.commons.error.{ErrorConstants, ErrorResponse}
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.commons.ws.{ApiConfig, JerseyRestClient}

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._

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

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, GenericRecord]] = {
    val species = propertyValue(SpeciesNameProperty, propertyValues)

    val json: util.LinkedHashMap[String, AnyRef] = Json.parseJson(Await.result(
      getAsEither(path = "/search",
        queryParams = List(
          ("scientificName", species),
          ("offset", offset.toString),
          ("limit", limit.toString))

      ), 20.seconds).right.get.readEntity(classOf[String])).asInstanceOf[util.LinkedHashMap[String, AnyRef]]
    offset = json.get("offset").asInstanceOf[Int]
    endOfRecords = json.get("endOfRecords").asInstanceOf[Boolean]
    val count = json.get("count").asInstanceOf[Int]

    noOfApiCalls = noOfApiCalls + 1

    if(count > 200000)
      List(Left(ErrorResponse(ErrorConstants.GlobalFlowErrorCode,"Invalid Request", 400, "Occurrence record count exceeds download limit (200000)")))
    else if(endOfRecords || noOfApiCalls > 1)
      List()
    else
      json.get("results").asInstanceOf[util.List[util.LinkedHashMap[String, AnyRef]]].asScala.map { value => {
        val gbifOccurrence = new GenericData.Record(AvroSchemaStore.get(schemaId).get)
        gbifOccurrence.put("scientificName", value.get("scientificName"))
        gbifOccurrence.put("decimalLongitude", value.get("decimalLongitude"))
        gbifOccurrence.put("decimalLatitude", value.get("decimalLatitude"))
        gbifOccurrence.put("institutionCode", value.get("institutionCode"))
        Right(gbifOccurrence)
      }}.toList
  }


  override def _relationships(): Set[RemoteRelationship] = {
    Set(RelationshipType.SUCCESS, RelationshipType.FAILURE)
  }


  override def metadata(): MetaData =
    MetaData(description =  "GBIF Occurrence Processor",
      tags = List("GBIF", "Occurrence", "Species").asJava)


  override def baseUrl(): String = "http://api.gbif.org/" + ApiVersion + "/occurrence"

  override def error(status: Int, message: String): ErrorResponse =
    ErrorResponse(ErrorConstants.GlobalFlowErrorCode, message, status)

  override def _properties(): List[RemoteProperty] = List(SpeciesNameProperty)

}
