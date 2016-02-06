package org.dcs.api.data.impl;

import java.io.File;
import java.io.InputStream;

import javax.inject.Named;
import javax.inject.Singleton;

import org.dcs.api.RESTException;
import org.dcs.api.data.FileDataManager;
import org.dcs.api.model.ErrorCode;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.config.ConfigurationFacade;
import org.dcs.config.DataConfiguration;
import org.dcs.data.reader.TableLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.Configurator;

/**
 * Created by cmathew on 27/01/16.
 */

@Named
@Singleton
public class FileDataManagerImpl implements FileDataManager {
	static final Logger logger = LoggerFactory.getLogger(FileDataManagerImpl.class);

	private File dataHome;
	
	private DataConfiguration configuration;

	public FileDataManagerImpl() throws RESTException {
		configuration = ConfigurationFacade.getCurrentDataConfiguration();
		dataHome = new File(configuration.getDataHomePath());
	}


	@Override
	public boolean delete(String dataSourceId) {
		if(dataHome.exists()) {
			return DataManagerUtils.delete(new File(dataHome.getAbsolutePath() + File.separator + dataSourceId));
		} else {
			logger.info("Data source with id : " + dataSourceId + " does not exist - ignoring delete");
		}
		return true;
	}


	@Override
	public void load(InputStream inputStream, String dataSourceName) throws RESTException {
    
    File dataSourceDir = new File(dataHome.getAbsolutePath() + File.separator + dataSourceName);
    if(dataSourceDir.exists()) {
      throw new RESTException(ErrorCode.DCS101());
    }
    dataSourceDir.mkdir();

    String dataSourcePath = dataSourceDir.getAbsolutePath() + File.separator + dataSourceName;
    
		TableLoader tableLoader = new TableLoader(inputStream, dataSourcePath);
		tableLoader.load();
	}

}
