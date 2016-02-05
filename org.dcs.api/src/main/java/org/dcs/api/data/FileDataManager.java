package org.dcs.api.data;

import java.io.InputStream;

import org.dcs.api.RESTException;

/**
 * Created by cmathew on 30/01/16.
 */
public interface FileDataManager {
  public String getDataStoreLocation();

  public boolean deleteDataStore();
  
  public boolean delete(String dataSourceId);

  void load(InputStream inputStream, String dataSourceName) throws  RESTException;
}
