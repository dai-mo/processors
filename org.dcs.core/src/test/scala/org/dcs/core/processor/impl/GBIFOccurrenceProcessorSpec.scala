package org.dcs.core.processor.impl

import org.dcs.core.CoreUnitSpec
import org.dcs.core.processor.GBIFOccurrenceProcessor

import scala.collection.JavaConverters._
import org.dcs.commons.serde.AvroImplicits._

import scala.collection.mutable

/**
  * Created by cmathew on 11.11.16.
  */
class GBIFOccurrenceProcessorSpec extends CoreUnitSpec {

    "The GBIF Occurrence Processor" should "return valid response" in {
      val processor = new GBIFOccurrenceProcessor()
      val response = processor
        .trigger("".getBytes,
          Map(GBIFOccurrenceProcessor.SpeciesNamePropertyKey -> "Loxodonta africana").asJava)
      response.foreach { result =>
        validate(result.deSerToJsonMap())
      }
    }


    def validate(record: mutable.Map[String, AnyRef]) {
      assert(record("genus").asInstanceOf[String] == "Loxodonta")
    }
}
