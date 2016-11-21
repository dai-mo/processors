package org.dcs.core.processor.impl

import java.io.File

import org.apache.avro.generic.GenericData
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitSpec
import org.dcs.core.processor.CSVFileOutputProcessor

import scala.collection.JavaConverters._
/**
  * Created by cmathew on 20.11.16.
  */
class CsvFileOutputProcessorSpec extends CoreUnitSpec {

  val schema = AvroSchemaStore.get("org.dcs.core.processor.TestResponseProcessor")
  val file: File = new File("target/csv-out.csv")
  file.delete()

  "The CSV File Output Processor" should "write the input avro record to file in csv format" in {
    val processor = new CSVFileOutputProcessor()
    val record1 = new GenericData.Record(schema.get)
    record1.put("response", "res1")
    val record2 = new GenericData.Record(schema.get)
    record2.put("response", "res2")
    val response = processor
      .execute(Some(record1),
        Map(CSVFileOutputProcessor.FileNamePropertyKey -> "csv-out",
          CSVFileOutputProcessor.FileBaseUrlPropertyKey -> "target").asJava)
    processor.onShutdown(null)
    file.delete()
  }
}
