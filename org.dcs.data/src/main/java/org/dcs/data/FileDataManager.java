package org.dcs.data;

import java.io.InputStream;
import java.util.UUID;

import org.dcs.api.service.RESTException;

/**
 * Created by cmathew on 30/01/16.
 */
public interface FileDataManager {
  
  public boolean delete(String dataSourceId);

  public UUID load(InputStream inputStream, String dataSourceName) throws  RESTException;
}
