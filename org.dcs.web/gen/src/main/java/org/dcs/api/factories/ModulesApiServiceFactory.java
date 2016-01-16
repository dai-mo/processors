package org.dcs.api.factories;

import org.dcs.api.ModulesApiService;
import org.dcs.core.service.ModulesApiServiceImpl;
import org.dcs.web.osgi.FrameworkService;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-15T18:33:44.874+01:00")
public class ModulesApiServiceFactory {

   //private final static ModulesApiService service = (ModulesApiService) FrameworkService.getService();

   public static ModulesApiService getModulesApi()
   {
//      ModulesApiService service = (ModulesApiService) FrameworkService.getService();
//      return service;

      return (ModulesApiService) FrameworkService.getService();
   }
}
