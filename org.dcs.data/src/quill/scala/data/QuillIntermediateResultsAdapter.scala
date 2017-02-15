package org.dcs.data

import java.util.Date

import org.apache.avro.Schema
import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.Provenance
import org.dcs.commons.config.DbConfig
import org.dcs.commons.serde.AvroImplicits._
import org.dcs.commons.serde.AvroSchemaStore

/**
  * Created by cmathew on 02.02.17.
  */
class QuillIntermediateResultsAdapter extends IntermediateResultsAdapter {

  protected val ctx = QuillContext

  def createContent(fdc: FlowDataContent): Unit = {
    import ctx._

    val contentCreate = quote(query[FlowDataContent].insert(lift(fdc)))
    ctx.run(contentCreate)
  }

  def createProvenance(fdp: FlowDataProvenance): Unit = {
    import ctx._

    val provenanceCreate = quote(query[FlowDataProvenance].insert(lift(fdp)))
    ctx.run(provenanceCreate)
  }


  def listProvenanceByComponentId(cid: String, maxResults: Int): List[Provenance] = {
    import org.dcs.api.data.FlowData._
    import ctx._

    val provenanceQuery = quote {
      for {
        fdp <- query[FlowDataProvenance].filter(_.componentId == lift(cid)).filter(_.eventType != "DROP").take(lift(maxResults))
        fdc <- query[FlowDataContent].filter(fdc => fdp.contentClaimIdentifier == fdc.id)
      } yield {
        (fdc.id, "", "", fdc.data, fdc.timestamp, fdp.attributes)
      }
    }

    var schema: Option[Schema] = None
    ctx.run(provenanceQuery).map(prov => {
      // FIXME: The avro deserialisation should finally move to the client side,
      //        with the schema store exposed as a service
      if (schema.isEmpty) schema = stringToMap(prov._6).get(RemoteProcessor.SchemaIdKey).flatMap(AvroSchemaStore.get)
      Provenance(prov._1, prov._2, prov._3, prov._4.deSerToJsonString(schema, schema), prov._5)
    }).sortBy(_.timestamp)(Ordering[Date].reverse)
  }

  def deleteProvenanceByComponentId(cid: String): Unit = {
    import ctx._

    val contentIdQuery = quote {
      query[FlowDataProvenance].filter(_.componentId == lift(cid))
    }

    val contentIds = ctx.run(contentIdQuery).map(fdp => fdp.contentClaimIdentifier)

    val deleteContentAction = quote {
      query[FlowDataContent].filter(fdc => liftQuery(contentIds).contains(fdc.id)).delete
    }
    ctx.run(deleteContentAction)

    val deleteProvenanceAction = quote {
      query[FlowDataProvenance].filter(_.componentId == lift(cid)).delete
    }
    ctx.run(deleteProvenanceAction)

  }

  def purge(): Unit = {
    import ctx._

    val truncateContent = quote {
      query[FlowDataContent].delete
    }
    val truncateProvenance = quote {
      query[FlowDataProvenance].delete
    }

    ctx.run(truncateContent)
    ctx.run(truncateProvenance)
  }
}
