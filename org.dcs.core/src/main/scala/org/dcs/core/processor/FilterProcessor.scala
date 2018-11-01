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

/**
  * Created by cmathew on 19.11.16.
  */


import java.util

import org.apache.avro.generic.GenericRecord
import org.dcs.api.processor._
import org.dcs.commons.error.ErrorResponse

import scala.collection.JavaConverters._
import org.dcs.api.processor.RelationshipType._

object FilterProcessor {

  def apply(): FilterProcessor = {
    new FilterProcessor()
  }

  val ContainsCmd = "contains"
  val StartsWithCmd = "starts with"

}

/**
  * Created by cmathew on 09.11.16.
  */
class FilterProcessor extends RemoteProcessor
  with Worker
  with FieldActions {

  import FilterProcessor._

  override def execute(record: Option[GenericRecord], propertyValues: util.Map[String, String]): List[Either[ErrorResponse, (String, GenericRecord)]] = {

    val isValid: Boolean = actions(propertyValues).map(a => a.name match {
      case ContainsCmd => a.fromJsonPath(record).value.asString.exists(s => s.contains(a.args))
      case StartsWithCmd => a.fromJsonPath(record).value.asString.exists(s => s.contains(a.args))
      case _ => false
    }).forall(identity)

    if(isValid)
      List(Right((Valid.id, record.get)))
    else
      List(Right((Invalid.id, record.get)))
  }


  override def _relationships(): Set[RemoteRelationship] = {
    Set(Valid, Invalid)
  }

  override def metadata(): MetaData =
    MetaData(description =  "Filter Processor",
      tags = List("filter"))

  override def _properties(): List[RemoteProperty] = Nil

  override def cmds: Set[Action] = Set(Action("contains", PropertyType.String),
    Action("starts with", PropertyType.String))
}

