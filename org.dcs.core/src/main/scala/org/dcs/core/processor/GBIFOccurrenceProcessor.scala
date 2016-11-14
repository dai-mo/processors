package org.dcs.core.processor

import java.util

import com.google.common.net.MediaType
import org.apache.avro.Schema
import org.apache.avro.data.Json
import org.dcs.api.processor._
import org.dcs.commons.error.{ErrorConstants, ErrorResponse}
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
    defaultValue =  "",
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
  with ApiConfig {

  import GBIFOccurrenceProcessor._

  val limit = 10
  var offset = 0
  var endOfRecords = false



  override def initState(): Unit = {
    offset = 0
    endOfRecords = false

  }

  override def execute(input: Array[Byte], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, Object]] = {
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


    if(count > 200000)
      List(Left(ErrorResponse(ErrorConstants.GlobalFlowErrorCode,"Invalid Request", 400, "Occurrence record count exceeds download limit (200000)")))
    else if(endOfRecords)
      List(Left(ErrorResponse(ErrorConstants.GlobalFlowErrorCode,"Invalid Request", 400, "End of stream")))
    else
      json.get("results").asInstanceOf[util.List[util.LinkedHashMap[String, AnyRef]]].asScala.map { value =>
        Right(value)
      }.toList
  }


  override def relationships(): util.Set[RemoteRelationship] = {
    val success = RemoteRelationship(RelationshipType.SucessRelationship,
      "All status updates will be routed to this relationship")
    Set(success).asJava
  }
  override def configuration: Configuration = {
    Configuration(inputMimeType = MediaType.PLAIN_TEXT_UTF_8.`type`(),
      outputMimeType = MediaType.JSON_UTF_8.`type`(),
      processorClassName =  this.getClass.getName,
      inputRequirementType = InputRequirementType.InputForbidden)
  }

  override def metadata(): MetaData =
    MetaData(description =  "GBIF Occurrence Processor",
      tags = List("GBIF", "Occurrence", "Species").asJava)

  override def properties(): util.List[RemoteProperty] =
    List(SpeciesNameProperty).asJava

  override def baseUrl(): String = "http://api.gbif.org/" + ApiVersion + "/occurrence"

  override def error(status: Int, message: String): ErrorResponse =
    ErrorResponse(ErrorConstants.GlobalFlowErrorCode, message, status)

  override def schema: Option[Schema] = None
}
