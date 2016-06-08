package org.dcs.core.api.service.impl

import org.dcs.api.error.RESTException
import org.dcs.core.BaseUnitSpec

class TestApiServiceSpec extends BaseUnitSpec {

	"The Test Api Service" should "throw a DCS001 error when no type is specified" in {
		val testApiService = new TestApiServiceImpl();
		intercept[RESTException] {
			testApiService.error(null)
		}
	}

	"The Test Api Service" should "throw a DCS001 error when an empty type is specified" in {
		val testApiService = new TestApiServiceImpl();
		intercept[RESTException] {			
			testApiService.error("")
		}
	}

	"The Test Api Service" should "throw an IllegalArgumentException when a non-empty type is specified" in {
		val testApiService = new TestApiServiceImpl();
		intercept[RESTException] {
			testApiService.error("unexpected")
		}
	}

	"The Test Api Service" should "return correct response for valid input" in {
		val testApiService = new TestApiServiceImpl();
		val user = "Bob";
		def out(user:String) = ("Hello " + user + ", This is DCS!");
		assertResult(out(user)) {
			testApiService.hello(user).response
		}

	}
}