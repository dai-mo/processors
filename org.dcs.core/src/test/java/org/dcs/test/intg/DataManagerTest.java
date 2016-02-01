package org.dcs.test.intg;


import org.apache.commons.io.FileUtils;
import org.dcs.api.data.DataManager;
import org.dcs.api.data.DataManagerException;
import org.dcs.api.data.impl.DataManagerImpl;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.impl.DataApiServiceImpl;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.test.DataUtils;
import org.dcs.test.ReflectionUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by cmathew on 27/01/16.
 */
@RunWith(Arquillian.class)
@Category(IntegrationTest.class)
public class DataManagerTest  extends CoreBaseTest{

  static final Logger logger = LoggerFactory.getLogger(DataManagerTest.class);

  @Deployment
  public static JavaArchive createDeployment() {
    return createBaseDeployment();
  }


  @Test
  public void testManageDataHomeDirectory() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DataManagerException, IOException {
    // check if multiple calls to create the home directory works fine
    ReflectionUtils.invokeMethod(dataManager, "createDataHomeDirectory");

    // check load of data
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");
    dataManager.loadDataSource(inputStream, "test.csv");

    File dataSourceFile = new File(dataManager.getDataHomePath() + "/test.csv/test.csv");
    assertTrue(dataSourceFile.exists());

    File dataInputFile = new File(DataUtils.getDataInputAbsolutePath(this.getClass())  + "/test.csv");
    assertEquals(FileUtils.readLines(dataInputFile), FileUtils.readLines(dataSourceFile));

  }

}
