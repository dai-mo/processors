package org.dcs.core.api.service.impl

import org.dcs.api.service.RESTException
import org.dcs.core.BaseUnitSpec

class TestApiServiceSpec extends BaseUnitSpec {

	"The Test Api Service" should "throw a DCS001 error when no type is specified" in {
		val testApiService = new TestApiServiceImpl();
		intercept[RESTException] {
			testApiService.testErrorGet(null)
		}
	}

	"The Test Api Service" should "throw a DCS001 error when an empty type is specified" in {
		val testApiService = new TestApiServiceImpl();
		intercept[RESTException] {			
			testApiService.testErrorGet("")
		}
	}

	"The Test Api Service" should "throw an IllegalArgumentException when a non-empty type is specified" in {
		val testApiService = new TestApiServiceImpl();
		intercept[IllegalStateException] {						
			testApiService.testErrorGet("unexpected")
		}
	}

	"The Test Api Service" should "return correct response for valid input" in {
		val testApiService = new TestApiServiceImpl();
		val user = "Bob";
		def out(user:String) = ("Hello " + user + "! This is DCS");
		assertResult(out(user)) {
			testApiService.testHelloGet(user).getResponse
		}
		testApiService.testHelloGet(null).getResponse should be (out("World"))
	}
}