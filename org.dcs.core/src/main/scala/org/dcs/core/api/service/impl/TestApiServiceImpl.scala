package org.dcs.core.api.service.impl

import javax.enterprise.inject.Default

import org.dcs.api.error.{ErrorConstants, RESTException}
import org.dcs.api.service.{TestApiService, TestResponse}
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider, Properties, Property};

@OsgiServiceProvider
@Properties(Array(
	new Property(name = "service.exported.interfaces", value = "*"),
	new Property(name = "service.exported.configs", value = "org.apache.cxf.ws")))
@Default
class TestApiServiceImpl extends TestApiService {

	override def error(eType: String): TestResponse =
    throw new RESTException(ErrorConstants.DCS001)
	
	override def hello(user: String): TestResponse = {
		TestResponse("Hello " + user + ", This is DCS!")
	}

}
