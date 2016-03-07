package org.dcs.core.api.service.impl;

import org.dcs.api.model.DataSource;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.RESTException;
import org.dcs.core.org.dcs.core.services.DataSourcesService;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@javax.annotation.Generated(value = "class org.dcs.swagger.DCSJavaJersey2ServerCodegen", date = "2016-03-02T15:34:09.482+01:00")
@OsgiServiceProvider
@Properties({
		@Property(name = "service.exported.interfaces", value = "*"),
		@Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class DataApiServiceImpl implements DataApiService {

	@Inject
	@OsgiService
  private DataSourcesService dataSourcesService;

	@Override
	public DataSource dataGet(DataSource filter) throws RESTException {
		dataSourcesService.getDataSources();
		return new DataSource();
	}

	@Override
	public DataSource dataPost(DataSource definition) throws RESTException {
		// do some magic!
		return new DataSource();
	}

}

