package io.swagger.api;

import io.swagger.api.factories.ModulesApiServiceFactory;

import io.swagger.annotations.ApiParam;

import io.swagger.model.Module;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/modules")

@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/modules", description = "the modules API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-14T16:15:36.482+01:00")
public class ModulesApi  {

   private final ModulesApiService delegate = ModulesApiServiceFactory.getModulesApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Modules", notes = "The Products endpoint returns the list of modules", response = Module.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "An array of products", response = Module.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = Module.class, responseContainer = "List") })

    public Response modulesGet(@ApiParam(value = "Type of module.") @QueryParam("type") List<String> type)
    throws NotFoundException {
        return delegate.modulesGet(type);
    }
}

