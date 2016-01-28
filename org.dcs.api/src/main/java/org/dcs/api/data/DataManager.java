package org.dcs.api.data;

import org.dcs.api.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by cmathew on 27/01/16.
 */
public class DataManager {
  static final Logger logger = LoggerFactory.getLogger(DataManager.class);

  private String dataHomePath = null;
  File dataHome;
  private static DataManager instance;

  private DataManager() {
    readConfig();
    init();
  }

  private DataManager(String dataHomePath) {
    this.dataHomePath = dataHomePath;
    init();
  }

  private void init() {
    dataHome = new File(dataHomePath);
    createDataHomeDirectory();
  }

  private void readConfig() {
    // read configuration from a yaml file here

    // for now the settings are hardcoded
    dataHomePath = "/data/home";
  }

  public static DataManager instance(String dataHomePath) {

    if(instance == null) {
      instance = new DataManager(dataHomePath);
    }
    return instance;
  }

  public static DataManager instance() {

    if(instance == null) {
      instance = new DataManager();
    }
    return instance;
  }

  public String getDataHomePath() {
    return dataHomePath;
  }

  private boolean createDataHomeDirectory() {
    if(dataHome.exists()) {
      logger.info("Data home directory already exists - ignoring create");
    } else {
      return dataHome.mkdir();
    }
    return true;
  }

  public boolean deleteDataHomeDirectory() {
    boolean delete = false;
    if(dataHome.exists()) {
      return delete(dataHome);
    } else {
      logger.info("Data home directory does not exist - ignoring delete");
    }
    return true;
  }

  private boolean delete(File fileOrDirToDelete) {
    if(fileOrDirToDelete.exists()) {
      if (fileOrDirToDelete.isDirectory()) {
        File[] files = fileOrDirToDelete.listFiles();
        for (int i = 0; i < files.length; i++) {
          if (files[i].isDirectory()) {
            delete(files[i]);
          } else {
            files[i].delete();
          }
        }
      } else {
        fileOrDirToDelete.delete();
      }
    }
    return fileOrDirToDelete.delete();
  }

  private boolean deleteDataHomeDirContents() {
    boolean areAllFilesDeleted = true;
    File[] files = dataHome.listFiles();
    logger.info("Deleting contents of data home directory ... ");
    for(File file : files) {
      boolean deleted = delete(file);
      areAllFilesDeleted = areAllFilesDeleted && deleted;
    }
    return areAllFilesDeleted;
  }

  private File[] getDataHomeDirectoryContents() {
    return dataHome.listFiles();
  }

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
