package org.dcs.core

import scala.reflect.runtime.universe
import scala.runtime.RichInt

import org.dcs.core.commons.Reflector

class ReflectorSpec extends BaseUnitSpec {
  
	"The Reflector Object" should "return the correct constructor for the given class" in {	  
		val const = Reflector.firstPrimaryConstructor[RichInt]
		val testString = const(1).asInstanceOf[RichInt]
		testString.getClass.getName should be ("scala.runtime.RichInt")
	}
}