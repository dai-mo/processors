package org.dcs.core.data;

import org.dcs.test.intg.CoreBaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class DataAdminTest extends CoreBaseTest {

  static final Logger logger = LoggerFactory.getLogger(FileDataManagerTest.class);

  @Deployment
  public static JavaArchive createDeployment() {
    return createBaseDeployment();
  }
}
