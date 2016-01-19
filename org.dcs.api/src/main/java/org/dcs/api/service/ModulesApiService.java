package org.dcs.api.service;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-18T18:08:07.141+01:00")
public interface ModulesApiService {
  
      public Response modulesGet(List<String> type,SecurityContext securityContext)
      throws NotFoundException;
  
}
