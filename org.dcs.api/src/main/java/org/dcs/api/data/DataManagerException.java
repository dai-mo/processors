package org.dcs.api.data;

import org.dcs.api.model.Error;
import org.dcs.api.RESTException;

/**
 * Created by cmathew on 27/01/16.
 */
public class DataManagerException extends RESTException {

  public DataManagerException(Error errorCode) {
    super(errorCode);
  }

  public DataManagerException(Error errorCode, Throwable t) {
    super(errorCode, t);
  }
}
