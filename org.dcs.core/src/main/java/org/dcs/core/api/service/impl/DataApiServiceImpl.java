package org.dcs.core.api.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Default;

import org.dcs.api.model.DataSource;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;


@OsgiServiceProvider
@OsgiService
@Properties({
		@Property(name = "service.exported.interfaces", value = "*"),
		@Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class DataApiServiceImpl implements DataApiService {

	@Override
	public List<DataSource> dataGet(DataSource filter) throws RESTException {		
		return Arrays.asList(new DataSource());
	}
	@Override
	public DataSource dataPost(DataSource definition) throws RESTException {
		return new DataSource();
	}
}

