package io.swagger.api.factories;

import io.swagger.api.ModulesApiService;
import io.swagger.api.impl.ModulesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-14T16:15:36.482+01:00")
public class ModulesApiServiceFactory {

   private final static ModulesApiService service = new ModulesApiServiceImpl();

   public static ModulesApiService getModulesApi()
   {
      return service;
   }
}
