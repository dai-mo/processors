package org.dcs.core.service

/**
  * Created by cmathew on 20.11.16.
  */

import javax.enterprise.inject.Default

import org.dcs.api.processor.RemoteProcessor
import org.dcs.api.service.StatefulRemoteProcessorService
import org.dcs.core.processor.{CSVFileOutputProcessor, ProcessorDefinitionStore}
import org.dcs.core.state.LocalStateManager
import org.ops4j.pax.cdi.api.{OsgiServiceProvider, Properties, Property}


@OsgiServiceProvider
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "org.dcs.api.service.StatefulRemoteProcessorService"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws"),
  new Property(name = "org.apache.cxf.ws.address", value = "/org/dcs/core/service/CSVFileOutputProcessorService"),
  new Property(name = "org.dcs.processor.tags", value = "csv,table"),
  new Property(name = "org.dcs.processor.type", value = "sink")
))
@Default
class CSVFileOutputProcessorService extends StatefulRemoteProcessorService
  with LocalStateManager
  with ProcessorDefinitionStore {

  override def init(): String = {
    CSVFileOutputProcessor().init(this)
  }

  override def initialise(): RemoteProcessor = {
    CSVFileOutputProcessor()
  }
}
