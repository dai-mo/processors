package org.dcs.api.service.impl;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.dcs.api.RESTException;
import org.dcs.api.service.ApiResponseMessage;
import org.dcs.api.service.ModulesApiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-18T12:04:30.211+01:00")
@OsgiServiceProvider
public class ModulesApiServiceImpl implements ModulesApiService {
  
      @Override
      public Response modulesGet(List<String> type,SecurityContext securityContext)
      throws RESTException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
}
