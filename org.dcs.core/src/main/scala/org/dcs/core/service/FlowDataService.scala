package org.dcs.core.service

import java.util
import java.util.{List => JavaList}
import javax.enterprise.inject.Default

import org.dcs.api.service.{IFlowDataService, Provenance}
import org.dcs.data.slick.SlickPostgresIntermediateResults
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

import scala.concurrent.Await
import scala.concurrent.duration.Duration


/**
  * Created by cmathew on 20.02.17.
  */
@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.IFlowDataService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/FlowDataService")
))
@Default
class FlowDataService extends IFlowDataService {


  def provenanceByComponentId(componentId: String, maxResults: Int): util.List[Provenance] = {
    Await.result(SlickPostgresIntermediateResults.listProvenanceByComponentId(componentId, maxResults), Duration.Inf)
  }

}
