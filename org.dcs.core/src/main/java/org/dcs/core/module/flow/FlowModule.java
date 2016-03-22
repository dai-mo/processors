package org.dcs.core.module.flow;

import java.util.List;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.Relationship;
import org.dcs.api.service.RESTException;

public interface FlowModule {
	
	public List<PropertyDescriptor> getPropertyDescriptors();
	
	public List<Relationship> getRelationships();
	
	public void schedule();
	
	public Object trigger(final ProcessContext context) throws RESTException ;
	
	public void unschedule();
	
	public void stop();
	
	public void shutdown();
	
	public void remove();

}
