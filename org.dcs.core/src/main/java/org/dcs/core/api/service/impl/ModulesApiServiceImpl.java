package org.dcs.core.api.service.impl;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import org.dcs.api.model.Module;
import org.dcs.api.service.ModulesApiService;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-18T12:04:30.211+01:00")
@OsgiServiceProvider
@Properties({
  @Property(name = "service.exported.interfaces", value = "*"),
  @Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
public class ModulesApiServiceImpl implements ModulesApiService {
  
      @Override
      public Module modulesGet(List<String> type) throws RESTException {
      Module module = new Module();
      module.setProductId("some id");
      module.setDescription("some description");
      module.setDisplayName("display name");
      
      return module;
  }
  
}
