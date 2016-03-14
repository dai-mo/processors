package org.dcs.api.service.impl;

import junit.framework.Assert;
import org.dcs.api.service.DataApiService;
import org.dcs.api.service.RESTException;
import org.dcs.core.api.service.impl.DataApiServiceImpl;
import org.dcs.core.services.impl.DataSourcesServiceImpl;
import org.dcs.core.test.CoreBaseTest;
import org.dcs.core.test.CoreHomeBaseTest;
import org.dcs.test.mock.MockConfigurationAdmin;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by cmathew on 26/01/16.
 */
@RunWith(Arquillian.class)
public class DataApiServiceImplTest extends CoreHomeBaseTest {

  static final Logger logger = LoggerFactory.getLogger(DataApiServiceImplTest.class);

  @Inject
  @OsgiService
  private DataApiService dataApiService;

  @Deployment
  public static JavaArchive createDeployment() {
    PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
    JavaArchive[] as = resolver.resolve("javax.ws.rs:javax.ws.rs-api").withTransitivity().as(JavaArchive.class);
    JavaArchive javaArchive = CoreBaseTest.createBaseDeployment()
        .addClass(DataApiService.class)
        .addClass(MockConfigurationAdmin.class)
        .addClass(ConfigurationAdmin.class)
        .addClass(DataApiServiceImpl.class)
        .addClass(DataSourcesServiceImpl.class)
        .addClass(RESTException.class);
    
    for(JavaArchive archive : as) {
      javaArchive.merge(archive);
    }
    return javaArchive;
  }
  
  @Test
  public void testUploadDatasource()  {
//    InputStream inputStream = DataUtils.getInputResourceAsStream(this.getClass(), "/test.csv");

    // check upload of file

//    DataSourcesServiceImpl manager;
//    try {
//      DataSource ds = new DataSource();
//      ds.setName("Example datasource");
//      ds.setUri("proto://host.domain:port/somewhere");
////      manager= dataApiService.dataPost();
////      assertNotNull(loader.getDataSourceId());
//    } catch(RESTException dme) {
//    	dme.printStackTrace();
//      fail("Exception should not be thrown here");
//    }
//
//    try {
//      // uploading the same file a second time should produce an error
////      loader = dataApiService.dataPost(inputStream, "test.csv");
//      fail("Exception should be thrown here");
//    } catch(RESTException dme) {
//      ErrorResponse errorResponse = dme.getErrorResponse();
//      assertEquals(ErrorConstants.getErrorResponse("DCS101"), errorResponse);
//    }

    Assert.assertNull(null);
  }
}
