package org.dcs.core.mock;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;

import javax.enterprise.inject.Default;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

@Default
public class MockBundleContext implements BundleContext {

	private static Map<String, Object> serviceNameObjectMap;
	
	public static void setServiceNameObjectMap(Map<String, Object> snom) {
		serviceNameObjectMap = snom;
	}
	
	@Override
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle installBundle(String location, InputStream input) throws BundleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle installBundle(String location) throws BundleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle[] getBundles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addServiceListener(ServiceListener listener, String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addServiceListener(ServiceListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeServiceListener(ServiceListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addBundleListener(BundleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeBundleListener(BundleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFrameworkListener(FrameworkListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeFrameworkListener(FrameworkListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public ServiceRegistration<?> registerService(String[] clazzes, Object service, Dictionary<String, ?> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceRegistration<?> registerService(String clazz, Object service, Dictionary<String, ?> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S> ServiceRegistration<S> registerService(Class<S> clazz, S service, Dictionary<String, ?> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S> ServiceRegistration<S> registerService(Class<S> clazz, ServiceFactory<S> factory,
			Dictionary<String, ?> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceReference<?>[] getServiceReferences(String clazz, String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceReference<?>[] getAllServiceReferences(String clazz, String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceReference<?> getServiceReference(String clazz) {
		MockServiceReference msr = new MockServiceReference();
		msr.setNameProperty(clazz);
		return msr;
	}

	@Override
	public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S> Collection<ServiceReference<S>> getServiceReferences(Class<S> clazz, String filter)
			throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getService(ServiceReference reference) {
		return serviceNameObjectMap.get(reference.getProperty(MockServiceReference.CLASS_NAME_KEY));
	}

	@Override
	public boolean ungetService(ServiceReference<?> reference) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDataFile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filter createFilter(String filter) throws InvalidSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBundle(String location) {
		// TODO Auto-generated method stub
		return null;
	}

}
