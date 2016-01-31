package org.dcs.api;

import java.io.File;

/**
 * Created by cmathew on 29/01/16.
 */
public class Configuration {

  private String dataRoot;

  private String dataRootPath;

  private String dataRootParentPath;


  public String getDataRoot() {
    return dataRoot;
  }


  public String getDataRootPath() {
    return dataRootParentPath + File.separator + dataRoot;
  }

  public String getDataRootParentPath() {
    return dataRootParentPath;
  }

}
