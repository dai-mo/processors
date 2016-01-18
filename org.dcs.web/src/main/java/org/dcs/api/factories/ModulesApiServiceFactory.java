package org.dcs.api.factories;

import org.dcs.api.service.ModulesApiService;
import org.dcs.web.osgi.FrameworkService;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-18T12:04:30.211+01:00")
public class ModulesApiServiceFactory {
   
   public static ModulesApiService getModulesApi()
   {
      return (ModulesApiService) FrameworkService.getService();
   }
}