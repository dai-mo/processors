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
import org.dcs.api.processor.CoreProperties._
import org.dcs.api.processor.{ProcessorSchemaField, PropertyType, RelationshipType}
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.commons.serde.JsonSerializerImplicits._
import org.dcs.core.processor.LatLongValidationProcessor
import org.dcs.core.{BaseProcessorUnitSpec, CoreUnitWordSpec}

import scala.collection.JavaConverters._
/**
  * Created by cmathew on 22.03.17.
  */
class LatLongValidationProcessorSpec  extends CoreUnitWordSpec
  with BaseProcessorUnitSpec {

  val defaultSchemaId = "org.dcs.core.processor.LatLong"
  addSchemaToStore(defaultSchemaId)

  "The LatLongValidation Processor" should  {
    val processor = new LatLongValidationProcessor()
    val schema = AvroSchemaStore.get(defaultSchemaId)
    val mappings = List(ProcessorSchemaField("latitude", PropertyType.String, "$.decimalLatitude"),
      ProcessorSchemaField("latitude", PropertyType.String, "$.latitude"),
      ProcessorSchemaField("longitude", PropertyType.String, "$.longitude")).toJson


    "return valid response for valid lat / longs" in {
      assert {
        val in = new GenericData.Record(schema.get)
        in.put("latitude", 50.0)
        in.put("longitude", 50.0)
        in.put("decimalLatitude", 50.0)


        val response = processor
          .execute(Some(in),
            Map(ReadSchemaIdKey -> defaultSchemaId, FieldsToMapKey -> mappings).asJava)
        val out = response.head.right.get
        out._2.get("latitude").asInstanceOf[Double] == 50 &&
          out._2.get("longitude").asInstanceOf[Double] == 50 &&
          out._1 == RelationshipType.Valid.id
      }
    }

    "return invalid response for invalid lat / longs" in {
      assert { val in = new GenericData.Record(schema.get)
        in.put("latitude", -100.0)
        in.put("longitude", 190.0)
        in.put("decimalLatitude", 50.0)

        val response = processor
          .execute(Some(in),
            Map(ReadSchemaIdKey -> defaultSchemaId, FieldsToMapKey -> mappings).asJava)
        response.head.right.get._1 == RelationshipType.Invalid.id
      }
    }
  }
}
