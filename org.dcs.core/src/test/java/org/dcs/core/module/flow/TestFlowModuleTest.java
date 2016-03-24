package org.dcs.core.module.flow;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.nifi.components.PropertyValue;
import org.dcs.api.model.TestResponse;
import org.dcs.api.service.FlowModuleConstants;
import org.dcs.api.service.ModuleFactoryService;
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

	@Inject	
	private ModuleFactoryService mFactory;

	@Deployment
	public static JavaArchive createDeployment() {
		return createBaseDeployment()
				.addClass(TestApiService.class)
				.addClass(TestApiServiceImpl.class)
				.addClass(FlowModule.class)
				.addClass(TestFlowModule.class)
				.addClass(ModuleFactoryService.class)
				.addClass(ModuleFactoryServiceImpl.class);
	}

	@Test
	public void testHello() {

		String moduleUUID = null;

		moduleUUID = mFactory.createFlowModule("org.dcs.core.module.flow.TestFlowModule");

		Assert.assertNotNull(moduleUUID);
		FlowModule module = ((ModuleFactoryServiceImpl)mFactory).getModule(moduleUUID);

		Assert.assertTrue(module instanceof TestFlowModule);

		String user = "Bob";

		try {
			Map<String, java.util.Properties> properties = new HashMap<>();
			Properties userNameProperties = new Properties();
			userNameProperties.put(FlowModuleConstants.PROPERTY_VALUE, user);
			properties.put(TestFlowModule.USER_NAME_ID, userNameProperties);
			
			String testResponse = new String(mFactory.trigger(moduleUUID, properties), StandardCharsets.UTF_8);
			Assert.assertEquals("Hello " + user + "! This is DCS", testResponse);
		} catch (RESTException e) {			
			e.printStackTrace();
			Assert.fail("Test Flow Module trigger failed");
		}
		mFactory.remove(moduleUUID);
		Assert.assertNull(((ModuleFactoryServiceImpl)mFactory).getModule(moduleUUID));

	}

}
