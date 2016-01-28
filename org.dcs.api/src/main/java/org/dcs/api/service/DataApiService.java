package org.dcs.api.service;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.dcs.api.data.DataManagerException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T14:30:34.903+01:00")
public interface DataApiService {
  
      public Response dataPost(InputStream inputStream, FormDataContentDisposition fileDetail, SecurityContext securityContext)
      throws DataManagerException;
  
}
