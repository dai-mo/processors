package org.dcs.core.data.impl;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.dcs.api.RESTException;
import org.dcs.api.model.ErrorCode;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.config.ConfigurationFacade;
import org.dcs.config.DataConfiguration;
import org.dcs.core.data.reader.TableLoader;
import org.dcs.data.FileDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 27/01/16.
 */

@Named
@Singleton
public class FileDataManagerImpl implements FileDataManager {
	static final Logger logger = LoggerFactory.getLogger(FileDataManagerImpl.class);

	private File dataHomeDir;
	
	private DataConfiguration configuration;
	
	@Inject
	DataAdmin dataAdmin;

	public FileDataManagerImpl() throws RESTException {
		configuration = ConfigurationFacade.getCurrentDataConfiguration();
		dataHomeDir = new File(configuration.getDataHomePath());
	}


	@Override
	public boolean delete(String dataSourceId) {
		if(dataHomeDir.exists()) {
			return DataManagerUtils.delete(new File(dataHomeDir.getAbsolutePath() + File.separator + dataSourceId));
		} else {
			logger.info("Data source with id : " + dataSourceId + " does not exist - ignoring delete");
		}
		return true;
	}


	@Override
	public UUID load(InputStream inputStream, String dataSourceName) throws RESTException {
    
    File dataSourceDir = new File(dataHomeDir.getAbsolutePath() + File.separator + dataSourceName);
    if(dataSourceDir.exists()) {
      throw new RESTException(ErrorCode.DCS101());
    }
    dataSourceDir.mkdir();

    String dataSourcePath = dataSourceDir.getAbsolutePath() + File.separator + dataSourceName;
    
		TableLoader tableLoader = new TableLoader(inputStream, dataSourcePath);
		tableLoader.load();
		
		return dataAdmin.addDataSource(dataSourceName, dataSourcePath);
	}

}