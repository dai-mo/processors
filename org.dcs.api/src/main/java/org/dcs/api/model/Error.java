package org.dcs.api.model;

import javax.ws.rs.core.Response;

/**
 * Created by cmathew on 27/01/16.
 */
public class Error {

  private String errorCode;
  private String message;
  private Response.Status httpStatus;

  public Error(String errorCode, String message, Response.Status httpStatus) {
    this.errorCode = errorCode;
    this.message = message;
    this.httpStatus = httpStatus;
  }


  public String getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }

  public Response.Status getHttpStatus() {
    return httpStatus;
  }

  public static Error DCS101() {
    return new Error("DCS101",
            "Datasource with given name already exists",
            Response.Status.NOT_ACCEPTABLE);
  }

  public static Error DCS102() {
    return new Error("DCS102",
            "Error storing data",
            Response.Status.INTERNAL_SERVER_ERROR);
  }

  @Override
  public boolean equals(Object object) {
    if(object instanceof Error && object != null) {
      Error that = (Error) object;
      if(this.errorCode.equals(that.errorCode) && this.httpStatus.equals(that.httpStatus)) {
        return true;
      }
    }
    return false;
  }
}
