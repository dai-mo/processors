package org.dcs.core.api.service.impl;

import javax.enterprise.inject.Default;

import org.dcs.api.model.ErrorConstants;
import org.dcs.api.model.TestResponse;
import org.dcs.api.service.RESTException;
import org.dcs.api.service.TestApiService;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

@OsgiServiceProvider
@OsgiService
@Properties({
    @Property(name = "service.exported.interfaces", value = "*"),
    @Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class TestApiServiceImpl implements TestApiService {

	@Override
	public TestResponse  testErrorGet(String type) throws RESTException {
		if(type != null && type.equals("unexpected")) {
			throw new IllegalStateException("Unexpected Error");
		} else {
			throw new RESTException(ErrorConstants.getErrorResponse("DCS001"));
		}			
	}

	@Override
	public TestResponse  testHelloGet(String user) throws RESTException {		
		String userToGreet = user == null || user.isEmpty()  ? "World" : user;
		TestResponse test = new TestResponse();
		test.setResponse("Hello " + userToGreet + "! This is DCS");
		return test;
	}

}
