package org.dcs.core.api.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Default;

import org.dcs.api.model.Module;
import org.dcs.api.service.ModulesApiService;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;


@OsgiServiceProvider
@Properties({
    @Property(name = "service.exported.interfaces", value = "*"),
    @Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class ModulesApiServiceImpl implements ModulesApiService {

  @Override
  public List<Module> modulesGet(List<String> type) throws RESTException {
    // do some magic!
    Module module = new Module();
    module.setProductId("some id");
    module.setDescription("some description");
    module.setDisplayName("display name");

		return Arrays.asList(module);
  }
}
