package org.dcs.web.osgi;

import org.dcs.api.service.ModulesApiService;
import org.dcs.osgi.FrameworkService;
import org.dcs.web.MockFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by cmathew on 19/01/16.
 */

public class FrameworkServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(FrameworkServiceTest.class);

  private static FrameworkService frameworkService;

  @BeforeClass
  public static void setup() throws Exception {

    frameworkService = new FrameworkService(MockFactory.getMockServletContext(FrameworkServiceTest.class, "dcs"));
    frameworkService.start();
  }

  @AfterClass
  public static void cleanup() {
    frameworkService.stop();
  }

  @Test
  public void testApiServicesLoad() {
    ModulesApiService service = (ModulesApiService) FrameworkService.getService(ModulesApiService.class.getName());
    assertNotNull(service);
  }

}
