package org.dcs.core.module.flow;

import java.util.Map;
import java.util.Properties;

import org.dcs.api.service.RESTException;
import org.osgi.framework.BundleContext;

public interface FlowModule {
	
  public void init(BundleContext bundleContext);
  
	public Map<String, Properties> getPropertyDescriptors();
	
	public Map<String, Properties> getRelationships();
	
	public void schedule();
	
	public byte[] trigger(Properties properties) throws RESTException ;
	
	public void unschedule();
	
	public void stop();
	
	public void shutdown();
	
	public void remove();

}
