package io.swagger.api;

import java.util.List;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-14T16:15:36.482+01:00")
public abstract class ModulesApiService {
  
      public abstract Response modulesGet(List<String> type)
      throws NotFoundException;
  
}
