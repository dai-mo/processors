package org.dcs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
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
  private static final String CONFIG_FILE_KEY = "config";
  protected Configuration config;


  public YamlConfigurator() {
    loadConfiguration();
  }

  protected void loadConfiguration() {
    try {
      String configFilePath = System.getProperty(CONFIG_FILE_KEY);
      if(configFilePath == null) {
        configFilePath = this.getClass().getResource(DEFAULT_CONFIG_FILE).getPath();
      }
      logger.info("Config file path : " + configFilePath);
      File configFile = new File(configFilePath);
      mapper = new ObjectMapper(new YAMLFactory());
      config = mapper.readValue(configFile, Configuration.class);

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
  public Configuration getConfiguration() {
    return config;
  }
}
