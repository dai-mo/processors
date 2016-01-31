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

/**
 * Created by cmathew on 29/01/16.
 */
@RunWith(Arquillian.class)
public class ConfiguratorTest {

  static final Logger logger = LoggerFactory.getLogger(ConfiguratorTest.class);

  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class)
            .addClass(Configurator.class)
            .addClass(YamlConfigurator.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Inject
  private Configurator configurator;

  @Test
  public void testConfigurationLoad() {
    Configuration config = configurator.getConfiguration();
    assertTrue(config.getDataRootPath().endsWith("/target/data"));
  }
}
