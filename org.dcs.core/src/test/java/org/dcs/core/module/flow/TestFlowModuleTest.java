package org.dcs.core.module.flow;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.dcs.api.service.FlowModuleConstants;
import org.dcs.api.service.ModuleFactoryService;
import org.dcs.api.service.RESTException;
import org.dcs.api.service.TestApiService;
import org.dcs.core.api.service.impl.TestApiServiceImpl;
import org.dcs.core.osgi.MockBundleContext;
import org.dcs.core.osgi.MockServiceReference;
import org.dcs.core.test.CoreBaseTest;
import org.dcs.core.test.CoreMockFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.ProviderType;


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
				.addClass(MockServiceReference.class)
				.addClass(MockBundleContext.class)
				.addClass(ModuleFactoryService.class)				
				.addClass(ModuleFactoryServiceImpl.class);
	}

	@Test
	public void testHello() {

		String className = "org.dcs.api.service.TestApiService";
		Map <String, Object> serviceNameObjectMap = new HashMap<>();
		serviceNameObjectMap.put(className, new TestApiServiceImpl());
		
		MockBundleContext.setServiceNameObjectMap(serviceNameObjectMap);
		String moduleUUID = null;

		moduleUUID = mFactory.createFlowModule("org.dcs.core.module.flow.TestFlowModule");

		Assert.assertNotNull(moduleUUID);
		FlowModule module = ((ModuleFactoryServiceImpl)mFactory).getModule(moduleUUID);

		Assert.assertTrue(module instanceof TestFlowModule);

		String user = "Bob";

		
		try {
			
			Properties valueProperties = new Properties();
			valueProperties.put(TestFlowModule.PROPERTY_USER_NAME_VALUE, user);
			
			String testResponse = new String(mFactory.trigger(moduleUUID, valueProperties), StandardCharsets.UTF_8);
			Assert.assertEquals("Hello " + user + "! This is DCS", testResponse);
		} catch (RESTException e) {			
			e.printStackTrace();
			Assert.fail("Test Flow Module trigger failed");
		}
		mFactory.remove(moduleUUID);
		Assert.assertNull(((ModuleFactoryServiceImpl)mFactory).getModule(moduleUUID));

	}

}
