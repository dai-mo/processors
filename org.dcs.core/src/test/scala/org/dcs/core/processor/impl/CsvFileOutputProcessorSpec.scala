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

import java.io.File

import org.apache.avro.generic.GenericData
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitFlatSpec
import org.dcs.core.processor.CSVFileOutputProcessor
import org.dcs.api.processor.CoreProperties._

import scala.collection.JavaConverters._
/**
  * Created by cmathew on 20.11.16.
  */
class CsvFileOutputProcessorSpec extends CoreUnitFlatSpec {

  val testResponseSchemaId = "org.dcs.core.processor.TestResponse"

  AvroSchemaStore.add(testResponseSchemaId)
  val file: File = new File("target/csv-out.csv")
  file.delete()

  "The CSV File Output Processor" should "write the input avro record to file in csv format" in {
    val processor = new CSVFileOutputProcessor()
    val record = new GenericData.Record(AvroSchemaStore.get(testResponseSchemaId).get)
    record.put("response", "res1")
    record.put("response", "res2")

    processor.
      execute(Some(record),
        Map(CSVFileOutputProcessor.FileNamePropertyKey -> "csv-out",
          CSVFileOutputProcessor.FileBaseUrlPropertyKey -> "target",
          ReadSchemaIdKey -> testResponseSchemaId).asJava)
    processor.
      execute(Some(record),
        Map(CSVFileOutputProcessor.FileNamePropertyKey -> "csv-out",
          CSVFileOutputProcessor.FileBaseUrlPropertyKey -> "target",
          ReadSchemaIdKey -> testResponseSchemaId).asJava)


    processor.onShutdown(null)
    file.delete()
  }
}
