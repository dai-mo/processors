package org.dcs.api;

import org.dcs.api.model.*;
import org.dcs.api.ModulesApiService;
import org.dcs.api.factories.ModulesApiServiceFactory;

import io.swagger.annotations.ApiParam;

import com.sun.jersey.multipart.FormDataParam;

import org.dcs.api.model.Error;
import org.dcs.api.model.Module;

import java.util.List;
import org.dcs.api.NotFoundException;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/modules")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the modules API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-15T18:35:39.348+01:00")
public class ModulesApi  {
   private final ModulesApiService delegate = ModulesApiServiceFactory.getModulesApi();

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Modules", notes = "The Products endpoint returns the list of modules", response = Module.class, responseContainer = "List", tags={ "Modules" })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "An array of products", response = Module.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = Module.class, responseContainer = "List") })

    public Response modulesGet(@ApiParam(value = "Type of module.") @QueryParam("type") List<String> type,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.modulesGet(type,securityContext);
    }
}
