package org.dcs.api.service;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import org.dcs.api.RESTException;
import org.dcs.api.model.Module;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-19T21:49:38.067+01:00")
public interface ModulesApiService {
  
      public Module modulesGet(List<String> type,SecurityContext securityContext)
      throws RESTException;
  
}
