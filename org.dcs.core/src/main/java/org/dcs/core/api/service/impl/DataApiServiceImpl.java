package org.dcs.core.api.service.impl;

import java.io.InputStream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import org.dcs.api.RESTException;
import org.dcs.api.model.DataLoader;
import org.dcs.api.service.DataApiService;
import org.dcs.data.FileDataManager;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T15:01:10.051+01:00")
@OsgiServiceProvider
@Default
public class DataApiServiceImpl implements DataApiService {
  static final Logger logger = LoggerFactory.getLogger(DataApiServiceImpl.class);

  @Inject
  @OsgiService
  private FileDataManager dataManager;

  @Override
  public DataLoader dataPost(InputStream inputStream, String fileName, SecurityContext securityContext)
          throws RESTException {
  	
    DataLoader dataLoader = new DataLoader();
    dataLoader.setDataSourceId(dataManager.load(inputStream, fileName).toString());
    return dataLoader;
  }

}
