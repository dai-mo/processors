package org.dcs.core.module.flow;

import java.util.UUID;

import org.apache.nifi.components.PropertyValue;
import org.dcs.api.model.TestResponse;
import org.dcs.api.service.RESTException;
import org.dcs.api.service.TestApiService;
import org.dcs.core.api.service.impl.TestApiServiceImpl;
import org.dcs.core.test.CoreBaseTest;
import org.dcs.core.test.CoreMockFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class TestFlowModuleTest extends  CoreBaseTest {

	static final Logger logger = LoggerFactory.getLogger(TestFlowModuleTest.class);

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

		moduleUUID = mFactory.createFlowModule("org.dcs.core.module.flow.TestFlowModule");

		Assert.assertNotNull(moduleUUID);
		FlowModule module = mFactory.getModule(moduleUUID);
		
		Assert.assertTrue(module instanceof TestFlowModule);

		String user = "Bob";
		PropertyValue propertyValue = CoreMockFactory.getMockPropertyValue(user);
		try {
			TestResponse testResponse = (TestResponse) mFactory.trigger(moduleUUID, CoreMockFactory.getMockProcessContext(TestFlowModule.USER_NAME, propertyValue));
			Assert.assertEquals("Hello " + user + "! This is DCS", testResponse.getResponse());
		} catch (RESTException e) {			
			e.printStackTrace();
			Assert.fail("Test Flow Module trigger failed");
		}
		mFactory.remove(moduleUUID);
		Assert.assertNull(mFactory.getModule(moduleUUID));

	}

}
