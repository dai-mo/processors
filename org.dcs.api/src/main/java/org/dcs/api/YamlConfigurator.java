package org.dcs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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

  private ObjectMapper mapper;
  private static final String CONFIG_FILE = "config.yaml";
  protected Configuration config;


  public YamlConfigurator() {
    loadConfiguration();
  }

  protected void loadConfiguration() {
    try {
      URL configURL = this.getClass().getResource(CONFIG_FILE);
      File configFile = new File(configURL.getPath());
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
