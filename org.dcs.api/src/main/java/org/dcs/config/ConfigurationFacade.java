package org.dcs.config;

import java.io.File;
import java.io.InputStream;

import javax.inject.Singleton;

import org.dcs.api.RESTException;
import org.dcs.api.utils.DataManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Created by cmathew on 29/01/16.
 */

public class ConfigurationFacade {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationFacade.class);
	private ObjectMapper mapper;
	private static final String DEFAULT_CONFIG_FILE_NAME = "config.yaml";
	private static final String CONFIG_FILE_KEY = "config";

	private DataConfiguration dataConfiguration;

	private static ConfigurationFacade configurationFacade;

	private String configFilePath;
	private String dataRootPath;


	public ConfigurationFacade() throws Exception {
		loadDataConfiguration();
	}

	public static ConfigurationFacade getInstance() {
		if(configurationFacade == null) {
			try {
				configurationFacade = new ConfigurationFacade();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}		
		return configurationFacade;
	}

	public DataConfiguration getDataConfiguration() {
		return dataConfiguration;
	}

	public static DataConfiguration getCurrentDataConfiguration() {
		return getInstance().getDataConfiguration();
	}


	public void loadDataConfiguration() throws Exception {
		try {
			mapper = new ObjectMapper(new YAMLFactory());

			configFilePath = System.getProperty(CONFIG_FILE_KEY);

			if(configFilePath == null) {
				InputStream inputStream = this.getClass().getResourceAsStream(DEFAULT_CONFIG_FILE_NAME);				
				if(inputStream == null) {
					throw new IllegalStateException("Could not load config file");
				} else {
					dataConfiguration = mapper.readValue(inputStream, DataConfiguration.class);    
				}
			} else {
				File configFile = new File(configFilePath);
				logger.warn("Config file path : " + configFilePath);
				dataConfiguration = mapper.readValue(configFile, DataConfiguration.class);      
			}    
			initialiseDataStore();
		} catch (Exception e) {
			throw e;
		}
	}

	private void initialiseDataStore() throws RESTException {
		DataManagerUtils.createDirectory(new File(dataConfiguration.getDataRootPath()));
		DataManagerUtils.createDirectory(new File(dataConfiguration.getDataHomePath()));
	}


	public boolean deleteDataStore() {	
		return DataManagerUtils.delete(new File(dataRootPath));
	}
}
