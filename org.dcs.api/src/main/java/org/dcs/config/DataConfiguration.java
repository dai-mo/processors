package org.dcs.config;

import java.io.File;

/**
 * Created by cmathew on 29/01/16.
 */
public class DataConfiguration {
	
  public final static String DATA_HOME_DIR_NAME = "home";
  public final static String DATA_ADMIN_DIR_NAME = "admin";
  public final static String DATA_ADMIN_DB_NAME = "data.db";

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
  
  public String getDataAdminPath() {
    return getDataRootPath() + File.separator + DATA_ADMIN_DIR_NAME;
  }
  
  public String getDataAdminDbPath() {
  	return getDataAdminPath() + File.separator + DATA_ADMIN_DB_NAME;
  }
  

}
