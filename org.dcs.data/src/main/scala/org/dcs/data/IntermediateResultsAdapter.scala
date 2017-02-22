package org.dcs.data

import java.util

import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.service.Provenance

import scala.concurrent.Future

/**
  * Created by cmathew on 15.02.17.
  */
trait IntermediateResultsAdapter {

  def createContent(fdc: FlowDataContent): Future[Unit]
  def createProvenance(fdp: FlowDataProvenance): Future[Unit]
  def listProvenanceByComponentId(cid: String, maxResults: Int): Future[util.List[Provenance]]
  def deleteProvenanceByComponentId(cid: String): Future[Int]
  def purge(): Future[Int]
}
