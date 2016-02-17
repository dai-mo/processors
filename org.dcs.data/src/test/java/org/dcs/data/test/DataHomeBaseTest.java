package org.dcs.data.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.dcs.data.utils.DataManagerUtils;
import org.junit.Before;

public class DataHomeBaseTest extends CoreBaseTest {
	
  @Before  
  public void testDeleteDataHomeDirContents() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
  	String dataHomePath = dataConfiguration.getDataHomePath();
    DataManagerUtils.deleteDirContents(new File(dataHomePath));
    File[] files = new File(dataHomePath).listFiles();
    assertTrue(files.length == 0);
  }

}
