package org.dcs.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import javax.inject.Inject;

import org.dcs.api.model.DataLoader;
import org.dcs.api.model.ErrorConstants;
import org.dcs.api.model.ErrorResponse;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.RESTException;
import org.dcs.core.api.service.impl.DataApiServiceImpl;
import org.dcs.core.test.CoreBaseTest;
import org.dcs.core.test.CoreHomeBaseTest;
import org.dcs.core.test.CoreMockFactory;
import org.dcs.test.DataUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
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
public class DataApiServiceImplTest extends CoreHomeBaseTest {

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
    FormDataContentDisposition fdcd =  CoreMockFactory.getFormDataContentDisposition("test.csv");

    DataLoader loader;
    try {
      loader = dataApiService.dataPost(inputStream, fdcd);
      assertNotNull(loader.getDataSourceId());
    } catch(RESTException dme) {
    	dme.printStackTrace();
      fail("Exception should not be thrown here");
    }

    try {
      // uploading the same file a second time should produce an error
      loader = dataApiService.dataPost(inputStream, fdcd);
      fail("Exception should be thrown here");
    } catch(RESTException re) {
      ErrorResponse errorResponse = re.getErrorResponse();
      assertEquals(ErrorConstants.DCS101(), errorResponse);
    }

  }
}
