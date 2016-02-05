package org.dcs.api.model;

import javax.ws.rs.core.Response;

/**
 * Created by cmathew on 27/01/16.
 */
public class ErrorCode {

  private String errorCode;
  private String message;
  private Response.Status httpStatus;

  public ErrorCode(String errorCode, String message, Response.Status httpStatus) {
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

  public static ErrorCode DCS101() {
    return new ErrorCode("DCS101",
            "Datasource with given name already exists",
            Response.Status.NOT_ACCEPTABLE);
  }

  public static ErrorCode DCS102() {
    return new ErrorCode("DCS102",
            "Error loading data",
            Response.Status.INTERNAL_SERVER_ERROR);
  }

  public static ErrorCode DCS103() {
    return new ErrorCode("DCS103",
            "Error initializing data store",
            Response.Status.INTERNAL_SERVER_ERROR);
  }
  
  public static ErrorCode DCS104() {
    return new ErrorCode("DCS104",
            "Error reading data",
            Response.Status.INTERNAL_SERVER_ERROR);
  }
  
  public static ErrorCode DCS105() {
    return new ErrorCode("DCS105",
            "Error writing data",
            Response.Status.INTERNAL_SERVER_ERROR);
  }

  @Override
  public boolean equals(Object object) {
    if(object instanceof ErrorCode && object != null) {
      ErrorCode that = (ErrorCode) object;
      if(this.errorCode.equals(that.errorCode) && this.httpStatus.equals(that.httpStatus)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public int hashCode() {
  	return errorCode.hashCode() + httpStatus.getStatusCode();
  }
}
