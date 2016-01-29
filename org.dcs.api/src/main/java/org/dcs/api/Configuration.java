package org.dcs.api;

import java.io.File;

/**
 * Created by cmathew on 29/01/16.
 */
public class Configuration {

  private String dataRoot;

  private String dataRootPath;

  public Configuration() {
    dataRootPath = File.separator + dataRootPath;
  }

  public String getDataRoot() {
    return dataRoot;
  }

  public void setDataRoot(String dataRoot) {
    this.dataRoot = dataRoot;
  }

  public String getDataRootPath() {
    return dataRootPath;
  }

  public void setDataRootPath(String dataRootPath) {
    this.dataRootPath = dataRootPath;
  }
}
