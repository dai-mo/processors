package org.dcs.api.data.impl;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.dcs.api.RESTException;
import org.dcs.api.data.FileDataManager;
import org.dcs.api.model.ErrorCode;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.config.Configuration;
import org.dcs.config.Configurator;
import org.dcs.config.YamlConfigurator;
import org.dcs.data.reader.TableLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 27/01/16.
 */

@Named
@Singleton
public class FileDataManagerImpl implements FileDataManager {
	static final Logger logger = LoggerFactory.getLogger(FileDataManagerImpl.class);

	private String dataRootPath;
	private File dataRoot;

	private String dataHomePath;
	private File dataHome;


	private Configurator configurator;


	public FileDataManagerImpl() throws RESTException {
		
		readConfig();
		dataRoot = new File(dataRootPath);
		dataHomePath = dataRootPath + File.separator + DataManagerUtils.DATA_HOME_DIR_NAME;
		dataHome = new File(dataHomePath);
		
		DataManagerUtils.createDirectory(dataRoot);
		DataManagerUtils.createDirectory(dataHome);
	}

	private void readConfig() {
		Configuration configuration = YamlConfigurator.getCurrentConfiguration();
		dataRootPath = configuration.getDataRootPath();
	}

	@Override
	public String getDataStoreLocation() {
		return dataHomePath;
	}

	private boolean createDataHomeDirectory() throws RESTException {
		if(dataHome.exists()) {
			logger.info("Data home directory " + dataHome.getAbsolutePath() + " already exists - ignoring create");
		} else {
			if(!dataHome.mkdir()) {
				throw new RESTException(ErrorCode.DCS103());
			}
		}
		return true;
	}

	private boolean createDataRootDirectory() throws RESTException {
		boolean created = true;
		if(dataRoot.exists()) {
			logger.info("Data root directory " + dataRoot.getAbsolutePath() + " already exists - ignoring create");
		} else {
			if(!dataRoot.mkdir()) {
				throw new RESTException(ErrorCode.DCS103());
			}
		}
		return true;
	}

	@Override
	public boolean deleteDataStore() {		
		if(dataHome.exists()) {
			return DataManagerUtils.delete(dataHome);
		} else {
			logger.info("Data home directory does not exist - ignoring delete");
		}
		return true;
	}


	@Override
	public boolean delete(String dataSourceId) {
		if(dataHome.exists()) {
			return DataManagerUtils.delete(new File(dataHomePath + File.separator + dataSourceId));
		} else {
			logger.info("Data source with id : " + dataSourceId + " does not exist - ignoring delete");
		}
		return true;
	}


	@Override
	public void load(InputStream inputStream, String dataSourceName) throws RESTException {
    String dataSourceDirPath = dataHomePath + File.separator + dataSourceName;
    File dataSourceDir = new File(dataSourceDirPath);
    if(dataSourceDir.exists()) {
      throw new RESTException(ErrorCode.DCS101());
    }
    dataSourceDir.mkdir();

    String dataSourcePath = dataSourceDirPath + File.separator + dataSourceName;
    
		TableLoader tableLoader = new TableLoader(inputStream, dataSourcePath);
		tableLoader.load();
	}

}
