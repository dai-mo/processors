package org.dcs.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import javax.inject.Inject;

import org.dcs.api.RESTException;
import org.dcs.api.data.FileDataManager;
import org.dcs.api.data.impl.FileDataManagerImpl;
import org.dcs.api.model.DataLoader;
import org.dcs.api.model.ErrorCode;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.NotFoundException;
import org.dcs.api.service.impl.DataApiServiceImpl;
import org.dcs.test.DataUtils;
import org.dcs.test.intg.CoreBaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 26/01/16.
 */
@RunWith(Arquillian.class)
public class DataApiServiceImplTest extends CoreBaseTest {

  static final Logger logger = LoggerFactory.getLogger(DataApiServiceImplTest.class);

  @Inject
  private DataApiService dataApiService;



  @Deployment
  public static JavaArchive createDeployment() {
    PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
    JavaArchive[] as = resolver.resolve("javax.ws.rs:javax.ws.rs-api").withTransitivity().as(JavaArchive.class);
    JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
            .addClass(FileDataManager.class)
            .addClass(FileDataManagerImpl.class)
            .addClass(DataApiService.class)
            .addClass(DataApiServiceImpl.class)
            .addClass(DataLoader.class)
            .addClass(RESTException.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    for(JavaArchive archive : as) {
      javaArchive.merge(archive);
    }
    return javaArchive;
  }

  @Test
  public void testloadFile() throws NotFoundException {
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");

    // check upload of file

    DataLoader loader;
    try {
      loader = dataApiService.dataPost(inputStream, "test.csv", null);
      assertNotNull(loader.getDataSourceId());
    } catch(RESTException dme) {
      fail("Exception should be thrown here");
    }

    try {
      // uploading the same file a second time should produce an error
      loader = dataApiService.dataPost(inputStream, "test.csv", null);
      fail("Exception should be thrown here");
    } catch(RESTException dme) {
      ErrorCode errorCode = dme.getErrorCode();
      assertEquals(ErrorCode.DCS101(), errorCode);
    }

  }
}
