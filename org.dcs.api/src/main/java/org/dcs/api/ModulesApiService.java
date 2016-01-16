package org.dcs.api;


import org.dcs.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-15T18:35:39.348+01:00")
public interface ModulesApiService {
  
      public Response modulesGet(List<String> type, SecurityContext securityContext)
      throws NotFoundException;
  
}
