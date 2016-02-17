package org.dcs.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import javax.inject.Inject;

import org.dcs.api.RESTException;
import org.dcs.api.model.DataLoader;
import org.dcs.api.model.ErrorCode;
import org.dcs.api.service.DataApiService;
import org.dcs.core.api.service.impl.DataApiServiceImpl;
import org.dcs.core.test.CoreBaseTest;
import org.dcs.core.test.DataHomeBaseTest;
import org.dcs.test.DataUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
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
public class DataApiServiceImplTest extends DataHomeBaseTest {

  static final Logger logger = LoggerFactory.getLogger(DataApiServiceImplTest.class);

  @Inject
  private DataApiService dataApiService;

  @Deployment
  public static JavaArchive createDeployment() {
    PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
    JavaArchive[] as = resolver.resolve("javax.ws.rs:javax.ws.rs-api").withTransitivity().as(JavaArchive.class);
    JavaArchive javaArchive = CoreBaseTest.createBaseDeployment()
            .addClass(DataApiService.class)
            .addClass(DataApiServiceImpl.class)
            .addClass(DataLoader.class)
            .addClass(RESTException.class);
    
    for(JavaArchive archive : as) {
      javaArchive.merge(archive);
    }
    return javaArchive;
  }
  
  @Test
  public void testloadFile()  {
    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");

    // check upload of file

    DataLoader loader;
    try {
      loader = dataApiService.dataPost(inputStream, "test.csv", null);
      assertNotNull(loader.getDataSourceId());
    } catch(RESTException dme) {
    	dme.printStackTrace();
      fail("Exception should not be thrown here");
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
