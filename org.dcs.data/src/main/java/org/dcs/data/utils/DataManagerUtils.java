package org.dcs.data.utils;

import org.dcs.api.model.ErrorConstants;
import org.dcs.api.service.RESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by cmathew on 01/02/16.
 */
public class DataManagerUtils {

  private static final Logger logger = LoggerFactory.getLogger(DataManagerUtils.class);

  public static boolean delete(File fileOrDirToDelete) {
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
      }
      return fileOrDirToDelete.delete();
    }
    logger.info("Ignoring deletion of " + fileOrDirToDelete.getAbsolutePath() + " since it does not exist");
    return false;
  }

  public static boolean deleteDirContents(File dir) {
    boolean areAllFilesDeleted = true;
    File[] files = dir.listFiles();
    if(files == null) {
    	return true;
    }
    logger.info("Deleting contents of directory " + dir.getAbsolutePath() + " ... ");
    for(File file : files) {
      boolean deleted = delete(file);
      areAllFilesDeleted = areAllFilesDeleted && deleted;
    }
    return areAllFilesDeleted;
  }
  
	public static boolean createDirectory(File dir) throws RESTException {		
		if(dir.exists()) {
			logger.info("Data root directory " + dir.getAbsolutePath() + " already exists - ignoring create");
		} else {
			if(!dir.mkdir()) {
				logger.info("Could not create data root directory at " + dir.getAbsolutePath());
				throw new RESTException(ErrorConstants.getErrorResponse("DCS103"));
			}
		}
		return true;
	}
}
