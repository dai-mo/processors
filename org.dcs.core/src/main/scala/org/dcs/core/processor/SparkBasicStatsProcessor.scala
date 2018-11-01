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


