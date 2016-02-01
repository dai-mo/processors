package org.dcs.api.data.impl;

import org.dcs.api.Configuration;
import org.dcs.api.Configurator;
import org.dcs.api.YamlConfigurator;
import org.dcs.api.data.DataManager;
import org.dcs.api.data.DataManagerException;
import org.dcs.api.model.Error;
import org.dcs.api.utils.DataManagerUtils;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.*;

/**
 * Created by cmathew on 27/01/16.
 */

//@Component
@Named
@Singleton
public class DataManagerImpl implements DataManager {
  static final Logger logger = LoggerFactory.getLogger(DataManagerImpl.class);

  private String dataRootPath;
  private File dataRoot;

  private String dataHomePath;
  private File dataHome;
  private static DataManagerImpl instance;

  private Configurator configurator;


  public DataManagerImpl() throws DataManagerException {
    this.configurator = new YamlConfigurator();

    readConfig();
    dataRoot = new File(dataRootPath);
    dataHomePath = dataRootPath + File.separator + DataManagerUtils.DATA_HOME_DIR_NAME;
    dataHome = new File(dataHomePath);
    createDataRootDirectory();
    createDataHomeDirectory();
  }

  private void readConfig() {
    Configuration configuration = configurator.loadConfiguration();
    dataRootPath = configuration.getDataRootPath();
  }

  @Override
  public String getDataHomePath() {
    return dataHomePath;
  }

  private boolean createDataHomeDirectory() throws DataManagerException {
    if(dataHome.exists()) {
      logger.info("Data home directory " + dataHome.getAbsolutePath() + " already exists - ignoring create");
    } else {
      if(!dataHome.mkdir()) {
        throw new DataManagerException(Error.DCS103());
      }
    }
    return true;
  }

  private boolean createDataRootDirectory() throws DataManagerException {
    boolean created = true;
    if(dataRoot.exists()) {
      logger.info("Data root directory " + dataRoot.getAbsolutePath() + " already exists - ignoring create");
    } else {
      if(!dataRoot.mkdir()) {
        throw new DataManagerException(Error.DCS103());
      }
    }
    return true;
  }

  @Override
  public boolean deleteDataHomeDirectory() {
    boolean delete = false;
    if(dataHome.exists()) {
      return DataManagerUtils.delete(dataHome);
    } else {
      logger.info("Data home directory does not exist - ignoring delete");
    }
    return true;
  }


  private File[] getDataHomeDirectoryContents() {
    return dataHome.listFiles();
  }

  @Override
  public void loadDataSource(InputStream inputStream, String dataSourceName) throws DataManagerException {
    String dataSourceDirPath = dataHomePath + File.separator + dataSourceName;
    File dataSourceDir = new File(dataSourceDirPath);
    if(dataSourceDir.exists()) {
      throw new DataManagerException(Error.DCS101());
    }
    dataSourceDir.mkdir();

    String dataDirPath = dataSourceDirPath + File.separator + dataSourceName;
    // write the inputStream to a FileOutputStream
    OutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(new File(dataDirPath));

      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }

    } catch (FileNotFoundException e) {
      throw new DataManagerException(Error.DCS102(),e);
    } catch (IOException e) {
      throw new DataManagerException(Error.DCS102(),e);
    } finally {
      try {
        inputStream.close();
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException e) {
        throw new DataManagerException(Error.DCS102(),e);
      }
    }
  }
}
