package org.dcs.api;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by cmathew on 27/01/16.
 */
@Provider
public class RESTExceptionHandler  implements ExceptionMapper<RESTException> {

  private static final Logger logger = (Logger) LoggerFactory.getLogger(RESTExceptionHandler.class);


  @Override
  public Response toResponse(RESTException re) {
    logger.debug("Rest Exception", re);
    return Response.status(re.getErrorCode().getHttpStatus()).entity(re.getErrorCode()).build();
  }
}
