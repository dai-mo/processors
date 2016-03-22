package org.dcs.core.module.flow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.Relationship;
import org.dcs.api.model.TestResponse;
import org.dcs.api.service.RESTException;
import org.dcs.api.service.TestApiService;
import org.ops4j.pax.cdi.api.OsgiService;

public class TestFlowModule implements FlowModule {

	@Inject
	@OsgiService
	private TestApiService testService;
	
  public static final PropertyDescriptor USER_NAME = new PropertyDescriptor.Builder()
      .name("User Name")
      .description("User To Greet")
      .required(false)
      .defaultValue("")
      .build();
  
  public static final Relationship REL_SUCCESS = new Relationship.Builder()
      .name("success")
      .description("All status updates will be routed to this relationship")
      .build();

	@Override
	public void init() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public List<PropertyDescriptor> getPropertyDescriptors() {
		final List<PropertyDescriptor> descriptors = new ArrayList<>();
    descriptors.add(USER_NAME);
    return descriptors;
	}

	@Override
	public List<Relationship> getRelationships() {
    final List<Relationship> relationships = new ArrayList<>();
    relationships.add(REL_SUCCESS);
    return relationships;
	}
	
	@Override
	public void schedule() {		
		
	}

	@Override
	public TestResponse trigger(final ProcessContext context) throws RESTException {
		return testService.testHelloGet(context.getProperty(USER_NAME).getValue());
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
