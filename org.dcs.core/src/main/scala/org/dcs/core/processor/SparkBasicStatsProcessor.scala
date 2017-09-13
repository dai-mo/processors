package org.dcs.core.processor

import java.util.{Map => JavaMap}

import org.dcs.api.processor.RelationshipType._
import org.dcs.api.processor._

/**
  * Created by cmathew on 09.11.16.
  */
object SparkBasicStatsProcessor {
  val AverageKey = "average"
  val CountKey = "count"

  def apply(): SparkBasicStatsProcessor = {
    new SparkBasicStatsProcessor()
  }

}

class SparkBasicStatsProcessor extends SparkLauncherBase
  with FieldsToMap
  with External {

  import SparkBasicStatsProcessor._


  override def _relationships(): Set[RemoteRelationship] = {
    Set(Success)
  }


  override def metadata(): MetaData =
    MetaData(description =  "Spark Basic Statistics Processor",
      tags = List("Spark", "Statistics"))



  override def _properties(): List[RemoteProperty] = Nil

  override def fields: Set[ProcessorSchemaField] =
    Set(ProcessorSchemaField(AverageKey, PropertyType.Double))

}


