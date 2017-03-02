package org.dcs.data

import org.dcs.api.data.{FlowDataContent, FlowDataProvenance}
import org.dcs.api.service.Provenance
import org.dcs.data.slick.Tables

import scala.concurrent.Future

/**
  * Created by cmathew on 15.02.17.
  */
trait IntermediateResultsAdapter {

  def getContent(contentId : String): Future[Option[Tables.FlowDataContentRow]]
  def createContent(fdc: FlowDataContent): Future[Unit]
  def incrementClaimaintCount(contentId : String): Future[Option[Int]]
  def decrementClaimaintCount(contentId : String): Future[Option[Int]]
  def getClaimantCount(contentId: String): Future[Option[Int]]
  def getContentSize: Future[Int]
  def deleteContent(contentId: String): Future[Int]
  def purgeContent(): Future[Int]


  def createProvenance(fdp: FlowDataProvenance): Future[Unit]
  def listProvenanceByComponentId(cid: String, maxResults: Int): Future[List[Provenance]]
  def getProvenanceSize: Future[Int]
  def deleteProvenanceByComponentId(cid: String): Future[Int]
  def purgeProvenance(): Future[Int]

  def purge(): Future[Int]
}
