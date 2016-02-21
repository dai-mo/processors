package org.dcs.core;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.dcs.api.service.DataApiService;
import org.dcs.core.api.service.impl.DataApiServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
  private ServiceRegistration<?> reg;

  private static Map<Object, String> implToServiceMap = new HashMap<>();
  
  public Activator() {
  	implToServiceMap.put(new DataApiServiceImpl(), DataApiService.class.getName());
  }
  
  public void start(BundleContext bc) throws Exception {        
      Dictionary<String, String> props = new Hashtable<>();
       
      String host = System.getProperty("service.host"); // obtain the current host name
      int port = Integer.valueOf(System.getProperty("service.port"));        // find a free port
      props.put("service.exported.interfaces", "*");
      props.put("service.exported.configs", "org.apache.cxf.ws");
      props.put("org.apache.cxf.ws.address", "http://" + host + ":" + port + "/");
       
      for(Map.Entry<Object, String> entry : implToServiceMap.entrySet()) {
      	reg = bc.registerService(entry.getValue(), entry.getKey(), props);
      }
  }

  public void stop(BundleContext bc) throws Exception {
      reg.unregister();
  }
}
