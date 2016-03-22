package org.dcs.core.module.flow;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.nifi.components.PropertyValue;
import org.dcs.api.model.TestResponse;
import org.dcs.api.service.RESTException;
import org.dcs.api.service.TestApiService;
import org.dcs.api.service.impl.DataApiServiceImplTest;
import org.dcs.core.api.service.impl.TestApiServiceImpl;
import org.dcs.core.module.flow.FlowModule;
import org.dcs.core.module.flow.ModuleException;
import org.dcs.core.module.flow.ModuleFactory;
import org.dcs.core.module.flow.TestFlowModule;
import org.dcs.core.test.CoreBaseTest;
import org.dcs.core.test.CoreHomeBaseTest;
import org.dcs.core.test.CoreMockFactory;
import org.dcs.test.mock.MockFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.api.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class TestFlowModuleTest extends  CoreBaseTest {
	
  static final Logger logger = LoggerFactory.getLogger(DataApiServiceImplTest.class);

  @Inject
  @OsgiService
  private TestApiService testService;

	

  @Deployment
  public static JavaArchive createDeployment() {
  	return createBaseDeployment()
  			.addClass(TestApiService.class)
  			.addClass(TestApiServiceImpl.class)
  			.addClass(FlowModule.class)
  			.addClass(TestFlowModule.class);
  }
  
  @Test
  public void testHello() {
  	  	
  	ModuleFactory mFactory = new ModuleFactory();
  	UUID moduleUUID = null;
  	try {
  		moduleUUID = mFactory.createFlowModule("org.dcs.core.module.flow.TestFlowModule");
		} catch (ModuleException e) {
			e.printStackTrace();
			Assert.fail("Test Flow Module not be initialised correctly");
		}
  	Assert.assertNotNull(moduleUUID);
  	FlowModule module = mFactory.getModule(moduleUUID);
  	Assert.assertTrue(module instanceof TestFlowModule);
  	
  	String user = "Bob";
  	PropertyValue propertyValue = CoreMockFactory.getMockPropertyValue(user);
  	try {
  		TestResponse testResponse = (TestResponse) module.trigger(CoreMockFactory.getMockProcessContext(TestFlowModule.USER_NAME, propertyValue));
			Assert.assertEquals("Hello " + user + "! This is DCS", testResponse.getResponse());
		} catch (RESTException e) {			
			e.printStackTrace();
			Assert.fail("Test Flow Module trigger failed");
		}
  	mFactory.removeModule(moduleUUID);
  	Assert.assertNull(mFactory.getModule(moduleUUID));
  	
  }

}
