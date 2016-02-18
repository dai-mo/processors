package org.dcs.test;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cmathew on 27/01/16.
 */
public class DataUtils {

  private static final String DATA_ROOT_DIR_NAME = "data";
  private static final String HOME_DATA_DIR_NAME = "home";
  private static final String INPUT_DATA_DIR_NAME = "input";

  private static final String TARGET_MARKER = "target";


  private static final String HOME_DATA_REL_DIR_NAME =
          File.separator + DATA_ROOT_DIR_NAME + File.separator + HOME_DATA_DIR_NAME;



  private static final String INPUT_DATA_REL_DIR_NAME =
          File.separator + DATA_ROOT_DIR_NAME + File.separator + INPUT_DATA_DIR_NAME;


  private static String TARGET_TEST_CLASSES_MARKER = "target" + File.separator + "test-classes";


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

  public static String getTargetDirectory(Class testClass) {
    URL testClassUrl = testClass.getResource(".");
    File testClassDir = new File(testClassUrl.getFile());
    String thisPath = testClassDir.getAbsolutePath();

    return thisPath.substring(0, thisPath.lastIndexOf(TARGET_MARKER) + TARGET_MARKER.length());
  }
  
  public static URL getTargetDirectoryUrl(Class testClass) throws MalformedURLException {
  	return new File(getTargetDirectory(testClass)).toURI().toURL();
  }

  public static String getTargetClassesDirectory(Class testClass) {
    URL thisUrl = testClass.getResource(".");
    File thisDir = new File(thisUrl.getFile());
    String thisPath = thisDir.getAbsolutePath();

    return thisPath
            .substring(0, thisPath.lastIndexOf(TARGET_TEST_CLASSES_MARKER) +
                    TARGET_TEST_CLASSES_MARKER.length())
            .replaceAll(TARGET_TEST_CLASSES_MARKER, "target" + File.separator + "classes");
  }
  
  public static String getFeatureDescriptorFileUrlString(Class testClass) throws MalformedURLException {
  	return DataUtils.getTargetDirectoryUrl(testClass) + File.separator  
  			+ "feature" + File.separator  
  			+ "feature.xml";
  }
  
  public static String getTestConfigurationFilePath(Class testClass) {
  	return DataUtils.getTargetDirectory(testClass) + File.separator  
		 + "test-classes" + File.separator  
		 + "config.yaml";
  }
  
  public static String getKarafConfigurationFilePath(Class testClass) {
  	return DataUtils.getTargetDirectory(testClass) + File.separator  
		 + "test-classes" + File.separator  
		 + "dcs-karaf-config.yaml";
  }

}
