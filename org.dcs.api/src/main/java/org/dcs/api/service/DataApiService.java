package org.dcs.api.service;

import org.dcs.api.data.DataManagerException;
import org.dcs.api.model.DataLoader;

import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T14:30:34.903+01:00")
public interface DataApiService {
  
      public DataLoader dataPost(InputStream inputStream, String fileName, SecurityContext securityContext)
      throws DataManagerException;
  
}
