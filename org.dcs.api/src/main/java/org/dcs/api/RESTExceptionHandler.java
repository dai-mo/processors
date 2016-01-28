package org.dcs.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by cmathew on 27/01/16.
 */
@Provider
public class RESTExceptionHandler  implements ExceptionMapper<RESTException> {

  @Override
  public Response toResponse(RESTException re) {
    return Response.status(re.getErrorCode().getHttpStatus()).entity(re.getErrorCode()).build();
  }
}
