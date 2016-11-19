package org.dcs.core.service

import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.RemoteProcessorService
import org.dcs.core.processor.{LatLongValidationProcessor, ProcessorDefinitionStore}
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

/**
  * Created by cmathew on 13.11.16.
  */
@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.RemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/LatLongValidationProcessorService")
))
@Default
class LatLongValidationProcessorService extends RemoteProcessorService
  with ProcessorDefinitionStore {


  override def initialise(): RemoteProcessor = {
    LatLongValidationProcessor()
  }
}