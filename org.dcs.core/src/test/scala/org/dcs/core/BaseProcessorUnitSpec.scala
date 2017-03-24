package org.dcs.core

import org.dcs.commons.serde.AvroSchemaStore

/**
  * Created by cmathew on 22.03.17.
  */
trait BaseProcessorUnitSpec  {

  def addSchemaToStore(schemaId: String): Unit = {
    val is = this.getClass.getResourceAsStream("/avro/" + schemaId + ".avsc")
    AvroSchemaStore.add(schemaId, is)
  }

}
