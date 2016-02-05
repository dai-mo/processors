package org.dcs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by cmathew on 29/01/16.
 */
@Default
@Singleton
public class YamlConfigurator implements Configurator {

  private static final Logger logger = LoggerFactory.getLogger(YamlConfigurator.class);
  private ObjectMapper mapper;
  private static final String DEFAULT_CONFIG_FILE = "config.yaml";
  private static final String TEST_CONFIG_FILE = "test_config.yaml";
  private static final String CONFIG_FILE_KEY = "config";
  private static final String RUN_MODE_KEY = "mode";
  private static final String TEST_MODE_VALUE = "test";
  private Configuration configuration;

  public YamlConfigurator() {
		configuration = loadConfiguration();
	}

  @Override
  public Configuration getConfiguration() {
  	return configuration;
  }
  
  public Configuration loadConfiguration() {
    try {
      return loadConfiguration(true);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }    
    return null;
  }

  public Configuration loadConfiguration(boolean exitOnError) throws IOException {
    try {
      String configFilePath = System.getProperty(CONFIG_FILE_KEY);
      if(configFilePath == null) {
        String mode = System.getProperty(RUN_MODE_KEY);
        if(mode != null && mode.equals(TEST_MODE_VALUE)) {
          configFilePath = this.getClass().getResource(TEST_CONFIG_FILE).getPath();
        } else {
          configFilePath = this.getClass().getResource(DEFAULT_CONFIG_FILE).getPath();
        }
      }
      logger.info("Config file path : " + configFilePath);
      File configFile = new File(configFilePath);
      mapper = new ObjectMapper(new YAMLFactory());
      return mapper.readValue(configFile, Configuration.class);      
    } catch (Exception e) {
      throw e;
    }
  }


}
