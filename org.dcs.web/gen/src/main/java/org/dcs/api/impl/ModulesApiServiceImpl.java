package org.dcs.api.impl;

import org.dcs.api.*;
import org.dcs.api.model.*;

import com.sun.jersey.multipart.FormDataParam;

import org.dcs.api.model.Error;
import org.dcs.api.model.Module;

import java.util.List;
import org.dcs.api.NotFoundException;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-15T15:51:46.850+01:00")
public class ModulesApiServiceImpl extends ModulesApiService {
  
      @Override
      public Response modulesGet(List<String> type,SecurityContext securityContext)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
}
