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

  val schema = AvroSchemaStore.get("org.dcs.core.processor.GBIFOccurrenceProcessor")
    "The GBIF Occurrence Processor" should "return valid response" in {
      val processor = new GBIFOccurrenceProcessor()
      val response = processor
        .trigger("".getBytes,
          Map(GBIFOccurrenceProcessor.SpeciesNamePropertyKey -> "Loxodonta africana").asJava)
      response.foreach { result =>
        validate(result.deSerToGenericRecord(schema, schema))
      }
    }


    def validate(record: GenericRecord) {
      assert(record.get("scientificName").toString.startsWith("Loxodonta"))
    }
}
