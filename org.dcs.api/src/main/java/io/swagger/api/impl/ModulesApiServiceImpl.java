package io.swagger.api.impl;

import io.swagger.api.*;

import java.util.List;
import io.swagger.api.NotFoundException;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-14T16:15:36.482+01:00")
public class ModulesApiServiceImpl extends ModulesApiService {
  
      @Override
      public Response modulesGet(List<String> type)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
}
