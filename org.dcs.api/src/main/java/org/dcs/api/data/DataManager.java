package org.dcs.api.data;

import org.dcs.api.data.DataManagerException;

import java.io.InputStream;

/**
 * Created by cmathew on 30/01/16.
 */
public interface DataManager {
  String getDataHomePath();

  boolean deleteDataHomeDirectory();

  void loadDataSource(InputStream inputStream, String dataSourceName) throws DataManagerException;
}
