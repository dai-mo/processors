package org.dcs.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.dcs.test.DataUtils;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 29/01/16.
 */
//@RunWith(Arquillian.class)
public class ConfiguratorTest {

  static final Logger logger = LoggerFactory.getLogger(ConfiguratorTest.class);


  @After
  public void postTest() {
  	System.clearProperty("config");
  	System.clearProperty("mode");
  }


  @Test
  public void testCorrectConfigurationLoad() {
    String userDefinedConfigFilePath = this.getClass().getResource("config.yaml").getPath();
    String defaultDataRootPath = DataUtils.getTargetDirectory(this.getClass()) + File.separator + "data";

    Configuration config = null;
    try {
      System.setProperty("config", userDefinedConfigFilePath);
      YamlConfigurator configurator = new YamlConfigurator(false);
      config = configurator.getConfiguration();
      assertEquals(defaultDataRootPath,config.getDataRootPath());

      System.clearProperty("config");
      configurator = new YamlConfigurator(false);
      config = configurator.getConfiguration();
      assertEquals(defaultDataRootPath,config.getDataRootPath());

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
      new YamlConfigurator(false);
      fail("An exception should be thrown when loading wrong config file");
    } catch (Exception e) {

    }
  }
}
