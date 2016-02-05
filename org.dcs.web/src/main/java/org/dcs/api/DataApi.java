package org.dcs.api;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.dcs.api.model.DataLoader;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.NotFoundException;
import org.dcs.osgi.FrameworkService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

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
            throws NotFoundException, RESTException {
        DataApiService delegate = (DataApiService) FrameworkService.getService(DataApiService.class.getName());
        DataLoader loader = delegate.dataPost(inputStream, fileDetail.getFileName(),securityContext);
        return Response.ok().entity(loader).build();
    }
}
