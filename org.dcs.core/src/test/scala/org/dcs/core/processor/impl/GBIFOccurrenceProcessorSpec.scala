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

import org.apache.avro.generic.GenericRecord
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitFlatSpec
import org.dcs.core.processor.GBIFOccurrenceProcessor

import scala.collection.JavaConverters._

/**
  * Created by cmathew on 11.11.16.
  */
class GBIFOccurrenceProcessorSpec extends CoreUnitFlatSpec {

    "The GBIF Occurrence Processor" should "return valid response" in {
      val processor = new GBIFOccurrenceProcessor()
      val schema = AvroSchemaStore.get(processor.schemaId)
      val response = processor
        .execute(None,
          Map(GBIFOccurrenceProcessor.SpeciesNamePropertyKey -> "Loxodonta africana").asJava)
      assert(response.size == 200)
      response.foreach { result =>
        val record = result.right.get
        assert(record._2.get("scientificName").toString.startsWith("Loxodonta"))
        assert(record._2.get("kingdom").toString.startsWith("Animalia"))
      }
    }

}
