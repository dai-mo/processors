package org.dcs.test.unit;

import org.dcs.api.data.DataManager;
import org.dcs.api.data.DataManagerException;
import org.dcs.api.model.DataLoader;
import org.dcs.api.model.Error;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.NotFoundException;
import org.dcs.test.DataUtils;
import org.dcs.test.ReflectionUtils;
import org.dcs.test.intg.IntegrationTest;
import org.dcs.test.paxe.PaxExamConfigOptionsFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Created by cmathew on 26/01/16.
 */
@RunWith(PaxExam.class)
//@ExamReactorStrategy(PerMethod.class)
@Category(IntegrationTest.class)
public class DataApiServiceImplTest {

  static final Logger logger = LoggerFactory.getLogger(DataApiServiceImplTest.class);

//  @Configuration
//  public Option[] config() {
//    return PaxExamConfigOptionsFactory.generateConfigOptions(this.getClass());
//  }

  @Inject
  private DataManager dataManager;

  @Inject
  //@OsgiService
  private DataApiService dataApiService;

  @Before
  public void testDeleteDataHomeDirContents() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    assertTrue(new File(dataManager.getDataHomePath()).exists());
    ReflectionUtils.invokeMethod(dataManager,"deleteDataHomeDirContents");
    assertTrue(new File(dataManager.getDataHomePath()).listFiles().length == 0);
  }

  @Test
  public void testloadFile() throws NotFoundException {
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");

    FormDataContentDisposition fdcd =
            FormDataContentDisposition.name("file").fileName("test.csv").build();

    // check upload of file

    Response response;
    try {
      response = dataApiService.dataPost(inputStream, fdcd, null);
      DataLoader loader = (DataLoader) response.getEntity();
      assertNotNull(loader.getDataSourceId());
    } catch(DataManagerException dme) {
      fail("Exception should be thrown here");
    }

    try {
      // uploading the same file a second time should produce an error
      response = dataApiService.dataPost(inputStream, fdcd, null);
      fail("Exception should be thrown here");
    } catch(DataManagerException dme) {
      Error errorCode = dme.getErrorCode();
      assertEquals(Error.DCS101(), errorCode);
    }

  }
}
