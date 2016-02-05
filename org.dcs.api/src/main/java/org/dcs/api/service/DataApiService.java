package org.dcs.api.service;

import java.io.InputStream;

import javax.ws.rs.core.SecurityContext;

import org.dcs.api.RESTException;
import org.dcs.api.model.DataLoader;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T14:30:34.903+01:00")
public interface DataApiService {
  
      public DataLoader dataPost(InputStream inputStream, String fileName, SecurityContext securityContext)
      throws RESTException;
  
}
