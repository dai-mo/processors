package org.dcs.core.data;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.FileUtils;
import org.dcs.api.RESTException;
import org.dcs.api.model.ErrorCode;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.test.DataUtils;
import org.dcs.test.ReflectionUtils;
import org.dcs.test.intg.CoreBaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 27/01/16.
 */
@RunWith(Arquillian.class)
public class FileDataManagerTest extends CoreBaseTest {

  static final Logger logger = LoggerFactory.getLogger(FileDataManagerTest.class);

  @Deployment
  public static JavaArchive createDeployment() {
    return createBaseDeployment();
  }


  @Before
  public void testDeleteDataHomeDirContents() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    assertTrue(new File(dataManager.getDataStoreLocation()).exists());
    DataManagerUtils.deleteDirContents(new File(dataManager.getDataStoreLocation()));
    assertTrue(new File(dataManager.getDataStoreLocation()).listFiles().length == 0);
  }
  
  @Test
  public void testManageDataHomeDirectory() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, RESTException {
    // check if multiple calls to create the home directory works fine
    ReflectionUtils.invokeMethod(dataManager, "createDataHomeDirectory");

    // check load of data
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");
    dataManager.load(inputStream, "test.csv");

    File dataSourceFile = new File(dataManager.getDataStoreLocation() + "/test.csv/test.csv");
    assertTrue(dataSourceFile.exists());

    File dataInputFile = new File(DataUtils.getDataInputAbsolutePath(this.getClass())  + "/valid-test.csv");
    assertEquals(FileUtils.readLines(dataInputFile), FileUtils.readLines(dataSourceFile));
    
    try {
    	dataManager.load(inputStream, "test.csv");
    	fail("DataManagerException should be thrown here");
    } catch(RESTException dme) {
    	assertEquals(ErrorCode.DCS101(), dme.getErrorCode());
    }
    
    File dataSourceDir = new File(dataManager.getDataStoreLocation() + "/test.csv");
    assertTrue(dataManager.delete("test.csv"));
    assertFalse(dataSourceDir.exists());
    
  }

}
