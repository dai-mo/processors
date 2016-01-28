package org.dcs.api;

import org.dcs.api.data.DataManagerException;
import org.dcs.api.service.DataApiService;

import org.dcs.api.model.DataLoader;

import org.dcs.api.service.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;


import org.dcs.osgi.FrameworkService;

@Path("/data")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the data API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T14:30:34.903+01:00")
public class DataApi  {

    @POST
    
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Upload data files", notes = "The data endpoint uploads files into the dcs workspace", response = DataLoader.class, tags={ "Data" })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "An array of products", response = DataLoader.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = DataLoader.class) })

    public Response dataPost(  @FormDataParam("file") InputStream inputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail,@Context SecurityContext securityContext)
            throws NotFoundException, DataManagerException {
        DataApiService delegate = (DataApiService) FrameworkService.getService(DataApiService.class.getName());
        return delegate.dataPost(inputStream, fileDetail,securityContext);
    }
}
