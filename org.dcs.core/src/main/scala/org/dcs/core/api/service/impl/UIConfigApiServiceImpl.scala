package org.dcs.core.api.service.impl

import org.dcs.api.service.UiConfigApiService
import org.dcs.api.model.UIConfig
import org.ops4j.pax.cdi.api.OsgiServiceProvider
import org.ops4j.pax.cdi.api.Properties
import org.ops4j.pax.cdi.api.Property
import javax.enterprise.inject.Default
import org.ops4j.pax.cdi.api.OsgiService
import org.osgi.service.cm.ConfigurationAdmin
import javax.inject.Inject


@OsgiServiceProvider
@OsgiService
@Properties(Array(
	new Property(name = "service.exported.interfaces", value = "*"),
	new Property(name = "service.exported.configs", value = "org.apache.cxf.ws")))
@Default
class UIConfigApiServiceImpl extends UiConfigApiService {
  val ConfigId = "org.dcs"
  val NifiUrl = "nifi.url"
  
  @Inject
  @OsgiService
  var configAdmin: ConfigurationAdmin = _
    
  def uiConfigGet(): UIConfig = {
    val nifiUrl = configAdmin.getConfiguration(ConfigId).getProperties.get(NifiUrl).asInstanceOf[String]
    val uiConfig = new UIConfig
    uiConfig.setNifiUrl(nifiUrl)
    
    uiConfig
  }
}