package org.dcs.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.inject.Inject;

import org.dcs.api.RESTException;
import org.dcs.api.model.DataSource;
import org.dcs.data.impl.DataAdmin;
import org.dcs.data.test.CoreBaseTest;
import org.dcs.data.config.DataConfiguration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(Arquillian.class)
public class DataAdminTest extends CoreBaseTest {

  static final Logger logger = LoggerFactory.getLogger(FileDataManagerTest.class);

  protected String defaultDataAdminPath = 
  		dataConfiguration.getDataRootPath() + File.separator + DataConfiguration.DATA_ADMIN_DIR_NAME;
  
  protected String defaultDataAdminDbPath = 
  		defaultDataAdminPath + File.separator + DataConfiguration.DATA_ADMIN_DB_NAME;

  @Deployment
  public static JavaArchive createDeployment() {
    return createBaseDeployment();
  }
  
  @Inject
  DataAdmin dataAdmin;
  
  @BeforeClass
  public static void deleteDataAdminContents() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
  	String dataAdminDbPath = dataConfiguration.getDataAdminDbPath();  	
   	File dataAdminDb = new File(dataAdminDbPath);
   	dataAdminDb.delete();
   	assertTrue(!dataAdminDb.exists());
    
  }
  
  @Test
  public void testCorrectDataAdminConfigurationLoad() {
    try {      
      assertEquals(defaultDataAdminPath,dataConfiguration.getDataAdminPath());
      assertEquals(defaultDataAdminDbPath,dataConfiguration.getDataAdminDbPath());
      assertTrue((new File(defaultDataAdminDbPath)).exists());      
    } catch (Exception e) {
      e.printStackTrace();
      fail("No exception should occur when correctly loading configuration");
    }
  }
  
  @Test
  public void testInsertDataSourceInfo() {
  	UUID uuid = null;
  	String name = "test";
  	String url = dataConfiguration.getDataHomePath() + File.separator + "test.csv";
  	
  	try {
			uuid = dataAdmin.addDataSource(name, url);
		} catch (RESTException e) {
			e.printStackTrace();
      fail("No exception should occur when correctly inserting data source info");
		}
  	
  	try {
			assertTrue(dataAdmin.getDataSources().contains(new DataSource(uuid, name, url)));
		} catch (RESTException e) {
			e.printStackTrace();
      fail("No exception should occur when correctly retrieving data source info");
		}
  }
}
