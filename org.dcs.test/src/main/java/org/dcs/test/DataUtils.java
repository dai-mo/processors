package org.dcs.test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by cmathew on 27/01/16.
 */
public class DataUtils {

  private static final String DATA_ROOT_DIR_NAME = "data";
  private static final String HOME_DATA_DIR_NAME = "home";
  private static final String INPUT_DATA_DIR_NAME = "input";


  private static final String HOME_DATA_REL_DIR_NAME =
          File.separator + DATA_ROOT_DIR_NAME + File.separator + HOME_DATA_DIR_NAME;



  private static final String INPUT_DATA_REL_DIR_NAME =
          File.separator + DATA_ROOT_DIR_NAME + File.separator + INPUT_DATA_DIR_NAME;




  public static String getHomeDataRelDirName() {
    return HOME_DATA_REL_DIR_NAME;
  }

  public static String getInputDataRelDirName() {
    return INPUT_DATA_REL_DIR_NAME;
  }

  public static String getDataHomeAbsolutePath(Class testClass) {
    URL dataRootURL = testClass.getResource(File.separator + DATA_ROOT_DIR_NAME);
    return dataRootURL.getPath() + File.separator + HOME_DATA_DIR_NAME;
  }

  public static String getDataInputAbsolutePath(Class testClass) {
    URL dataRootURL = testClass.getResource(File.separator + DATA_ROOT_DIR_NAME);
    return dataRootURL.getPath() + File.separator + INPUT_DATA_DIR_NAME;
  }

  public static String getDataRootParentPath(Class testClass) {
    return  testClass.getResource(".").getPath();
  }

  public static InputStream getInputResourceAsStream(Class testClass, String inputRelPath) {
    String dataInputTestFilePath = DataUtils.getInputDataRelDirName() + inputRelPath;
    return testClass.getResourceAsStream(dataInputTestFilePath);
  }

  public static String getDataInputFileAbsolutePath(Class testClass, String relFilePath) {
    URL dataRootURL = testClass.getResource(File.separator + DATA_ROOT_DIR_NAME);
    return dataRootURL.getPath() + File.separator + INPUT_DATA_DIR_NAME + File.separator + relFilePath;
  }

}
