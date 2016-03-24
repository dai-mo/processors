package org.dcs.core.osgi;

import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class MockServiceReference implements ServiceReference<Object> {
	
	public static final String CLASS_NAME_KEY = "className";
	Properties properties;
	
	public MockServiceReference() {
		properties = new Properties();
	}

	public void setNameProperty(String className) {
		properties.put(CLASS_NAME_KEY, className);
	}
	
	@Override
	public Object getProperty(String key) {
		return properties.get(CLASS_NAME_KEY);
	}

	@Override
	public String[] getPropertyKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle[] getUsingBundles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAssignableTo(Bundle bundle, String className) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(Object reference) {
		// TODO Auto-generated method stub
		return 0;
	}

}
