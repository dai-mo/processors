package org.dcs.core.service

import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.StatefulRemoteProcessorService
import org.dcs.core.processor.{ProcessorDefinitionStore, SparkBasicStatsProcessor}
import org.dcs.core.state.LocalStateManager
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

/**
  * Created by cmathew on 13.11.16.
  */
@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.StatefulRemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/SparkBasicStatsProcessorService"),
  new Property(name = "org.dcs.processor.tags", value = "spark,statistics,average,mean"),
  new Property(name = "org.dcs.processor.type", value = "external")
))
@Default
class SparkBasicStatsProcessorService extends StatefulRemoteProcessorService
  with LocalStateManager
  with ProcessorDefinitionStore {

  override def init(): String = {
    SparkBasicStatsProcessor().init(this)
  }

  override def initialise(): RemoteProcessor = {
    SparkBasicStatsProcessor()
  }
}
