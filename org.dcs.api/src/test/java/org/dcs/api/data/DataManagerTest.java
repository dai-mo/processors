package org.dcs.api.data;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.dcs.test.BaseDataTest;
import org.dcs.test.DataUtils;
import org.dcs.test.ReflectionUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Created by cmathew on 27/01/16.
 */
public class DataManagerTest extends BaseDataTest {

  protected static DataManager dataManager;

  @BeforeClass
  public static void dataManagerSetup() {
    dataManager = DataManager.instance(dataHomeAbsolutePath);
    assertEquals(dataHomeAbsolutePath, dataManager.getDataHomePath());
    assertTrue((new File(dataHomeAbsolutePath)).exists());
  }

  @AfterClass
  public static void dataManagerCleanup() {
    dataManager.deleteDataHomeDirectory();
    assertTrue(!(new File(dataHomeAbsolutePath)).exists());
  }

  @Test
  public void testDeleteDataHomeDirContents() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    ReflectionUtils.invokeMethod(dataManager,"deleteDataHomeDirContents");
  }

  @Test
  public void testManageDataHomeDirectory() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DataManagerException, IOException {
    // check if multiple calls to create the home directory works fine
    ReflectionUtils.invokeMethod(dataManager, "createDataHomeDirectory");
    assertTrue((new File(dataHomeAbsolutePath)).exists());

    // check load of data
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");
    dataManager.loadDataSource(inputStream, "test.csv");

    File dataSourceFile = new File(dataHomeAbsolutePath + "/test.csv/test.csv");
    assertTrue(dataSourceFile.exists());

    File dataInputFile = new File(dataInputAbsolutePath  + "/test.csv");
    assertEquals(FileUtils.readLines(dataInputFile), FileUtils.readLines(dataSourceFile));

  }

}
