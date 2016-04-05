package org.dcs.core.api.service.impl

import javax.enterprise.inject.Default

import org.dcs.api.model.ErrorConstants
import org.dcs.api.model.TestResponse
import org.dcs.api.service.RESTException
import org.dcs.api.service.TestApiService
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

@OsgiServiceProvider
@OsgiService
@Properties(Array(
	new Property(name = "service.exported.interfaces", value = "*"),
	new Property(name = "service.exported.configs", value = "org.apache.cxf.ws")))
@Default
class TestApiServiceImpl extends TestApiService {

	override def testErrorGet(errorType: String): TestResponse = Option(errorType) match {
		case Some(errorType) if (!errorType.isEmpty) => throw new IllegalStateException("Unexpected Error")
		case _ => throw new RESTException(ErrorConstants.getErrorResponse("DCS001"));
	}
	
	override def testHelloGet(user: String): TestResponse = {
		val test: TestResponse = new TestResponse()
		test.setResponse("Hello " + Option(user).getOrElse("World") + "! This is DCS");
		test
	}

}
