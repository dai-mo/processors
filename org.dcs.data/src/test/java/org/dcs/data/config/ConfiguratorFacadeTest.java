package org.dcs.data.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.dcs.data.utils.DataManagerUtils;
import org.dcs.test.DataUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 29/01/16.
 */
public class ConfiguratorFacadeTest {

  static final Logger logger = LoggerFactory.getLogger(ConfiguratorFacadeTest.class);
  
  String userDefinedConfigFilePath = DataUtils.getTestConfigurationFilePath(this.getClass());
  String defaultDataRootPath = DataUtils.getTargetDirectory(this.getClass()) + File.separator + "data";
  String defaultDataHomePath = defaultDataRootPath + File.separator + DataConfiguration.DATA_HOME_DIR_NAME;
  String defaultDataAdminPath = defaultDataRootPath + File.separator + DataConfiguration.DATA_ADMIN_DIR_NAME;
  String defaultDataAdminDbPath = defaultDataAdminPath + File.separator + DataConfiguration.DATA_ADMIN_DB_NAME;
  

  @Before
  public void testDeleteDataHomeDirContents() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {    
    DataManagerUtils.delete(new File(defaultDataRootPath));
    assertTrue(!new File(defaultDataRootPath).exists());
  }

  @After
  public void postTest() {
  	System.clearProperty("config");
  	System.clearProperty("mode");
  }


  @Test
  public void testCorrectFileStoreConfigurationLoad() {   
    String testDataRootPath = "." + File.separator + "target" + File.separator + "data";
    String testDataHomePath = testDataRootPath + File.separator + DataConfiguration.DATA_HOME_DIR_NAME;

    
    DataConfiguration config = null;
    try {
      System.setProperty("config", userDefinedConfigFilePath);
      ConfigurationFacade configurator = new ConfigurationFacade();
      config = configurator.getDataConfiguration();
      assertEquals(testDataRootPath,config.getDataRootPath());
      assertEquals(testDataHomePath,config.getDataHomePath());
      assertTrue((new File(testDataHomePath)).exists());
      
      System.clearProperty("config");
      configurator = new ConfigurationFacade();
      config = configurator.getDataConfiguration();
      assertEquals(testDataRootPath,config.getDataRootPath());
      assertEquals(testDataHomePath,config.getDataHomePath());      

      
    } catch (Exception e) {
      e.printStackTrace();
      fail("No exception should occur when correctly loading configuration");
    }
  }

  @Test
  public void testIncorrectConfigurationLoad() {
    String userDefinedConfigFilePath = this.getClass().getResource(".").getPath() + "wrong-config.yaml";
    
    try {
      System.setProperty("config", userDefinedConfigFilePath);
      new ConfigurationFacade();
      fail("An exception should be thrown when loading wrong config file");
    } catch (Exception e) {
    	
    }
    System.clearProperty("config");
  }
  
}
  
  