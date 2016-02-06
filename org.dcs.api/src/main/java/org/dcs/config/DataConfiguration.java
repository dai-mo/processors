package org.dcs.config;

import java.io.File;

/**
 * Created by cmathew on 29/01/16.
 */
public class DataConfiguration {
	
  public final static String DATA_HOME_DIR_NAME = "home";

  private String dataRoot;

  private String dataRootParentPath;

  public String getDataRoot() {
    return dataRoot;
  }


  public String getDataRootParentPath() {
    return dataRootParentPath;
  }
  
  
  public String getDataRootPath() {
    return dataRootParentPath + File.separator + dataRoot;
  }
  
  public String getDataHomePath() {
    return getDataRootPath() + File.separator + DATA_HOME_DIR_NAME;
  }

}
