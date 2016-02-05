package org.dcs.config;

import java.io.File;
import java.io.InputStream;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Created by cmathew on 29/01/16.
 */
@Singleton
public class YamlConfigurator implements Configurator {

	private static final Logger logger = LoggerFactory.getLogger(YamlConfigurator.class);
	private ObjectMapper mapper;
	private static final String DEFAULT_CONFIG_FILE_NAME = "config.yaml";
	private static final String CONFIG_FILE_KEY = "config";

	private Configuration configuration;

	public YamlConfigurator() {
		configuration = loadConfiguration();
	}

	public YamlConfigurator(boolean exitOnError) throws Exception {
		configuration = loadConfiguration(exitOnError);
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	public Configuration loadConfiguration() {
		try {
			return loadConfiguration(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}    
		return null;
	}

	public Configuration loadConfiguration(boolean exitOnError) throws Exception {
		try {
			mapper = new ObjectMapper(new YAMLFactory());

			String configFilePath = System.getProperty(CONFIG_FILE_KEY);
			
			if(configFilePath == null) {
				InputStream inputStream = this.getClass().getResourceAsStream(DEFAULT_CONFIG_FILE_NAME);				
				if(inputStream == null) {
					throw new IllegalStateException("Could not load config file");
				} else {
					return mapper.readValue(inputStream, Configuration.class);    
				}
			} else {
				File configFile = new File(configFilePath);
				logger.warn("Config file path : " + configFilePath);
				return mapper.readValue(configFile, Configuration.class);      
			}    
			
		} catch (Exception e) {
			throw e;
		}
	}


}
