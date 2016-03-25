package org.dcs.core.module.flow;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.dcs.api.model.TestResponse;
import org.dcs.api.service.FlowModuleConstants;
import org.dcs.api.service.RESTException;
import org.dcs.api.service.TestApiService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TestFlowModule implements FlowModule {

	
	private TestApiService testService;

	
  public static final Map<String, Properties> properties = new HashMap<>();
  
  public static final Map<String, Properties> relationships = new HashMap<>();
  
  

  public static final String PROPERTY_USER_NAME_VALUE = "User Name";

	@Override
	public void init(BundleContext bundleContext) {		
    ServiceReference reference = bundleContext.getServiceReference(TestApiService.class.getName());
    testService = (TestApiService) bundleContext.getService(reference);
    
		Properties userNameProperties = new Properties();
		userNameProperties.put(FlowModuleConstants.PROPERTY_NAME, PROPERTY_USER_NAME_VALUE);
		userNameProperties.put(FlowModuleConstants.PROPERTY_DESCRIPTION, "User To Greet");
		userNameProperties.put(FlowModuleConstants.PROPERTY_REQUIRED, false);
		userNameProperties.put(FlowModuleConstants.PROPERTY_DEFAULT_VALUE, "");
		
		properties.put(PROPERTY_USER_NAME_VALUE, userNameProperties);
		
		Properties userNameRelationships = new Properties();
		userNameRelationships.put(FlowModuleConstants.PROPERTY_NAME, FlowModuleConstants.REL_SUCCESS_ID);
		userNameRelationships.put(FlowModuleConstants.PROPERTY_DESCRIPTION, "All status updates will be routed to this relationship");
		
		relationships.put(FlowModuleConstants.REL_SUCCESS_ID, userNameRelationships);
	}
	
	@Override
	public Map<String, Properties> getPropertyDescriptors() {
    return properties;
	}

	@Override
	public Map<String, Properties> getRelationships() {
    return relationships;
	}
	
	@Override
	public void schedule() {		
		
	}

	@Override
	public byte[] trigger(Properties properties) throws RESTException {		
		TestResponse response= testService.testHelloGet(properties.getProperty(PROPERTY_USER_NAME_VALUE));
		return response.getResponse().getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void unschedule() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
