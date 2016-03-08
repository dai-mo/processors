package org.dcs.core.api.service.impl;

import org.dcs.api.model.DataSource;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "class org.dcs.swagger.DCSJavaJersey2ServerCodegen", date = "2016-03-02T15:34:09.482+01:00")
@OsgiServiceProvider
@OsgiService
@Properties({
		@Property(name = "service.exported.interfaces", value = "*"),
		@Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class DataApiServiceImpl implements DataApiService {

	@Override
	public Response dataGet(DataSource filter) throws RESTException {
		// do some magic!
		return Response.ok().build();
	}
	@Override
	public Response dataPost(DataSource definition) throws RESTException {
		// do some magic!
		return Response.ok().build();
	}

}

