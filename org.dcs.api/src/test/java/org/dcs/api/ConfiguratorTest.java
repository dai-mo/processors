package org.dcs.api;

import static org.junit.Assert.*;

import org.dcs.test.DataUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;

/**
 * Created by cmathew on 29/01/16.
 */
//@RunWith(Arquillian.class)
public class ConfiguratorTest {

  static final Logger logger = LoggerFactory.getLogger(ConfiguratorTest.class);





  @Test
  public void testCorrectConfigurationLoad() {
    String userDefinedConfigFilePath = this.getClass().getResource("config.yaml").getPath();
    String defaultDataRootPath = System.getProperty("user.home") + File.separator + "data";

    Configuration config = null;
    try {
      System.setProperty("config", userDefinedConfigFilePath);
      YamlConfigurator configurator = new YamlConfigurator();
      config = configurator.loadConfiguration(false);
      assertEquals(defaultDataRootPath,config.getDataRootPath());

      System.clearProperty("config");
      configurator = new YamlConfigurator();
      config = configurator.loadConfiguration(false);
      assertEquals(defaultDataRootPath,config.getDataRootPath());

      System.setProperty("mode", "test");
      configurator = new YamlConfigurator();
      config = configurator.loadConfiguration(false);
      assertTrue(config.getDataRootPath().endsWith("/target/data"));
    } catch (Exception e) {
      e.printStackTrace();
      fail("No exception should occur when correctly loading configuration");
    }
  }

  @Test
  public void testIncorrectConfigurationLoad() {
    String userDefinedConfigFilePath = this.getClass().getResource(".").getPath() + "wrong-config.yaml";
    String defaultDataRootPath = System.getProperty("user.home") + File.separator + "data";

    Configuration config = null;
    try {
      System.setProperty("config", userDefinedConfigFilePath);
      YamlConfigurator configurator = new YamlConfigurator();
      config = configurator.loadConfiguration(false);
      assertEquals(defaultDataRootPath,config.getDataRootPath());
      fail("An exception should be thrown when loading wrong config file");
    } catch (Exception e) {

    }
  }
}
