package org.dcs.api;

import org.dcs.api.model.ErrorCode;

/**
 * Created by cmathew on 27/01/16.
 */
public class RESTException extends Exception {


  private ErrorCode errorCode;

  public RESTException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public RESTException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public RESTException(ErrorCode errorCode, Throwable t) {
    super(t);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

}
