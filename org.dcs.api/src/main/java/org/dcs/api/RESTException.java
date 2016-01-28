package org.dcs.api;

import org.dcs.api.model.Error;

/**
 * Created by cmathew on 27/01/16.
 */
public class RESTException extends Exception {


  private Error errorCode;

  public RESTException(Error errorCode) {
    this.errorCode = errorCode;
  }

  public RESTException(Error errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public RESTException(Error errorCode, Throwable t) {
    super(t);
    this.errorCode = errorCode;
  }

  public Error getErrorCode() {
    return errorCode;
  }

}
