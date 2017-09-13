package org.dcs.core.service

import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.RemoteProcessorService
import org.dcs.core.processor.{DataGeneratorProcessor, ProcessorDefinitionStore}
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}


@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.RemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/DataGeneratorProcessorService"),
  new Property(name = "org.dcs.processor.tags", value = "generator,prototype"),
  new Property(name = "org.dcs.processor.type", value = "ingestion")
))
@Default
class DataGeneratorProcessorService extends RemoteProcessorService
  with ProcessorDefinitionStore {


  override def initialise(): RemoteProcessor = {
    DataGeneratorProcessor()
  }
}
