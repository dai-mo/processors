package org.dcs.core.service

import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.StatefulRemoteProcessorService
import org.dcs.core.processor.{ProcessorDefinitionStore, StatefulTestProcessor}
import org.dcs.core.state.LocalStateManager
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

/**
  * Created by cmathew on 06/09/16.
  */
@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.StatefulRemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/StatefulTestProcessorService"),
  new Property(name = "org.dcs.processor.tags", value = "test,stateful"),
  new Property(name = "org.dcs.processor.type", value = "worker")
))
@Default
class StatefulTestProcessorService extends StatefulRemoteProcessorService
  with LocalStateManager
  with ProcessorDefinitionStore {

  override def init(): String = {
    StatefulTestProcessor().init(this)
  }

  override def initialise(): RemoteProcessor = {
    StatefulTestProcessor()
  }
}
