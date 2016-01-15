package org.dcs.core.service;

import org.dcs.api.ApiResponseMessage;
import org.dcs.api.ModulesApiService;
import org.dcs.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-15T18:33:44.874+01:00")
public class ModulesApiServiceImpl extends ModulesApiService {
  
      @Override
      public Response modulesGet(List<String> type,SecurityContext securityContext)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
}
