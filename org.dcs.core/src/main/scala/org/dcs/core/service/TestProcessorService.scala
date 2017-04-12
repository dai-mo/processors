package org.dcs.core.service

import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.RemoteProcessorService
import org.dcs.core.processor.{ProcessorDefinitionStore, TestProcessor}
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}

/**
  * Created by cmathew on 06/09/16.
  */
@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.RemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/TestProcessorService"),
  new Property(name = "org.dcs.processor.tags", value = "test,stateless"),
  new Property(name = "org.dcs.processor.type", value = "worker")
))
@Default
class TestProcessorService extends RemoteProcessorService
  with ProcessorDefinitionStore {

  override def initialise(): RemoteProcessor = TestProcessor()
}
