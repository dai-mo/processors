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

package org.dcs.core.processor.impl

import org.apache.avro.generic.GenericData
import org.dcs.api.processor.{Action, PropertyType, RelationshipType}
import org.dcs.api.processor.CoreProperties._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.commons.serde.JsonSerializerImplicits._
import org.dcs.core.processor.FilterProcessor
import org.dcs.core.{BaseProcessorUnitSpec, CoreUnitWordSpec}

import scala.collection.JavaConverters._

/**
  * Created by cmathew on 27.03.17.
  */
class FilterProcessorSpec extends CoreUnitWordSpec
  with BaseProcessorUnitSpec {

  val schemaId = "org.dcs.core.processor.Filter"
  addSchemaToStore(schemaId)

  "The Filter Processor" should  {
    val processor = new FilterProcessor()
    val schema = AvroSchemaStore.get(schemaId)

    val fieldActions = List(Action(FilterProcessor.ContainsCmd, PropertyType.String, "$.first_name", "Ob")).toJson

    val FirstNameKey = "first_name"
    val FirstName = "Obi"

    val MiddleNameKey = "middle_name"
    val MiddleName = "Wan"

    val LastNameKey = "last_name"
    val LastName = "Kenobi"

    val AgeKey = "age"
    val Age = 9999


    val person = new GenericData.Record(schema.get)
    person.put(FirstNameKey, FirstName)
    person.put(MiddleNameKey, MiddleName)
    person.put(LastNameKey, LastName)
    person.put(AgeKey, Age)

    "return valid response for filtered output" in {
      assert {
        val out = processor
          .execute(Some(person),
            Map(ReadSchemaIdKey -> schemaId, FieldActionsKey -> fieldActions).asJava)
        out.head.right.get._2 == person &&
          out.head.right.get._1 == RelationshipType.Valid.id
      }
    }

    val invalidFieldActions = List(Action("$.first_name", FilterProcessor.ContainsCmd, "Luke")).toJson
    "return valid response for non-filtered output" in {
      assert(processor
        .execute(Some(person),
          Map(ReadSchemaIdKey -> schemaId, FieldActionsKey -> invalidFieldActions).asJava)
        .head.right.get._1 == RelationshipType.Invalid.id)

    }
  }
}
