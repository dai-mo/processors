package org.dcs.data

import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.service.Provenance

/**
  * Created by cmathew on 15.02.17.
  */
trait IntermediateResultsAdapter {

  def createContent(fdc: FlowDataContent): Unit
  def createProvenance(fdp: FlowDataProvenance): Unit
  def listProvenanceByComponentId(cid: String, maxResults: Int): List[Provenance]
  def deleteProvenanceByComponentId(cid: String): Unit
  def purge(): Unit
}
