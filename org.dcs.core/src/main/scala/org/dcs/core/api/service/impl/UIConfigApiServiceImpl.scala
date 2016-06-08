package org.dcs.core.api.service.impl

import javax.enterprise.inject.Default

import org.dcs.api.service.{UIConfig, UIConfigApiService}
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider, Properties, Property}


@OsgiServiceProvider
@OsgiService
@Properties(Array(
	new Property(name = "service.exported.interfaces", value = "*"),
	new Property(name = "service.exported.configs", value = "org.apache.cxf.ws")))
@Default
class UIConfigApiServiceImpl extends UIConfigApiService {

  override def config(): UIConfig = UIConfig("http://localhost:8888/nifi")
}