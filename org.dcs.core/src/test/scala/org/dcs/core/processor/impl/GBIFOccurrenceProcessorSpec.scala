package org.dcs.core.processor.impl

import org.apache.avro.generic.GenericRecord
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore
import org.dcs.core.CoreUnitSpec
import org.dcs.core.processor.GBIFOccurrenceProcessor

import scala.collection.JavaConverters._

/**
  * Created by cmathew on 11.11.16.
  */
class GBIFOccurrenceProcessorSpec extends CoreUnitSpec {


    "The GBIF Occurrence Processor" should "return valid response" in {
      val processor = new GBIFOccurrenceProcessor()
      val schema = AvroSchemaStore.get(processor.schemaId)
      val response = processor
        .trigger("".getBytes,
          Map(GBIFOccurrenceProcessor.SpeciesNamePropertyKey -> "Loxodonta africana").asJava)
      response.grouped(3).foreach { result =>
        validate(result(2).deSerToGenericRecord(schema, schema))
      }
    }


    def validate(record: GenericRecord) {
      assert(record.get("scientificName").toString.startsWith("Loxodonta"))
    }
}
