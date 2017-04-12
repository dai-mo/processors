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
      }
    }

}
