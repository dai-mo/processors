package org.dcs.api;

import io.swagger.annotations.ApiParam;
import org.dcs.api.model.Module;
import org.dcs.api.service.ModulesApiService;
import org.dcs.api.service.NotFoundException;
import org.dcs.osgi.FrameworkService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/modules")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the modules API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-19T21:49:38.067+01:00")
public class ModulesApi  {
   private final ModulesApiService delegate = (ModulesApiService) FrameworkService.getService(ModulesApiService.class.getName());

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Modules", notes = "The Products endpoint returns the list of modules", response = Module.class, responseContainer = "List", tags={ "Modules" })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "An array of products", response = Module.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = Module.class, responseContainer = "List") })

    public Response modulesGet(@ApiParam(value = "Type of module.") @DefaultValue("") @QueryParam("type") List<String> type,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.modulesGet(type,securityContext);
    }
}
