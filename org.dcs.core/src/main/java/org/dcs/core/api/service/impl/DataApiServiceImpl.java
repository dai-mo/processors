package org.dcs.core.api.service.impl;

import java.io.InputStream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.dcs.api.model.DataLoader;
import org.dcs.api.model.DataSource;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.RESTException;
import org.dcs.data.FileDataManager;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T15:01:10.051+01:00")
@OsgiServiceProvider
@Properties({
	@Property(name = "service.exported.interfaces", value = "*"),
	@Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class DataApiServiceImpl implements DataApiService {
	static final Logger logger = LoggerFactory.getLogger(DataApiServiceImpl.class);


	@Inject
	@OsgiService
	private FileDataManager dataManager;

	@Override
	public DataSource dataGet() throws RESTException {
		// do some magic!
		return new DataSource();
	}

	@Override
	public DataLoader dataPost(InputStream inputStream, FormDataContentDisposition fileDetail)
			throws RESTException {
    DataLoader dataLoader = new DataLoader();
    dataLoader.setDataSourceId(dataManager.load(inputStream, fileDetail.getFileName()).toString());
    return dataLoader;

	}
}
