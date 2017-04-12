package org.dcs.core.service
import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.RemoteProcessorService
import org.dcs.core.processor.{FilterProcessor, ProcessorDefinitionStore}
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

/**
  * Created by cmathew on 13.11.16.
  */
@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.RemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/FilterProcessorService"),
  new Property(name = "org.dcs.processor.tags", value = "filter,validation"),
  new Property(name = "org.dcs.processor.type", value = "worker")
))
@Default
class FilterProcessorService extends RemoteProcessorService
  with ProcessorDefinitionStore {


  override def initialise(): RemoteProcessor = {
    FilterProcessor()
  }
}
