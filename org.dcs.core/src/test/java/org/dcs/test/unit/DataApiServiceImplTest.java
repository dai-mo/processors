package org.dcs.test.unit;

import org.dcs.api.data.DataManager;
import org.dcs.api.data.DataManagerException;
import org.dcs.api.model.DataLoader;
import org.dcs.api.service.NotFoundException;
import org.dcs.api.service.impl.DataApiServiceImpl;
import org.dcs.test.BaseDataTest;
import org.dcs.test.DataUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by cmathew on 26/01/16.
 */
public class DataApiServiceImplTest extends BaseDataTest {

  private static DataManager dataManager;

  @BeforeClass
  public static void dataManagerSetup() {
    dataManager = DataManager.instance(dataHomeAbsolutePath);
  }

  @AfterClass
  public static void dataManagerCleanup() {
    dataManager.deleteDataHomeDirectory();
  }

  @Test
  public void testloadFile() throws NotFoundException, DataManagerException {
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");

    FormDataContentDisposition fdcd =
            FormDataContentDisposition.name("file").fileName("test.csv").build();

    DataApiServiceImpl dasi = new DataApiServiceImpl();
    Response response = dasi.dataPost(inputStream, fdcd, null);
    DataLoader loader = (DataLoader) response.getEntity();
    assertNotNull(loader.getDataSourceId());
  }
}
