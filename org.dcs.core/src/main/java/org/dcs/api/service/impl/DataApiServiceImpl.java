package org.dcs.api.service.impl;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.dcs.api.data.DataManager;
import org.dcs.api.data.DataManagerException;
import org.dcs.api.model.DataLoader;
import org.dcs.api.service.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import org.osgi.service.component.annotations.Component;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T15:01:10.051+01:00")
@Component
public class DataApiServiceImpl implements DataApiService {

  @Override
  public Response dataPost(InputStream inputStream, FormDataContentDisposition fileDetail, SecurityContext securityContext)
          throws DataManagerException {

    UUID uuid = UUID.randomUUID();
    DataManager dataManager = DataManager.instance();
    dataManager.loadDataSource(inputStream, fileDetail.getFileName());
    DataLoader dataLoader = new DataLoader();
    dataLoader.setDataSourceId(uuid.toString());
    return Response.ok().entity(dataLoader).build();
  }

}
