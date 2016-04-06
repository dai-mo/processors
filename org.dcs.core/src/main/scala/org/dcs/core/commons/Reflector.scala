package org.dcs.core.commons

import scala.reflect.runtime.{ universe => ru }
import scala.reflect.runtime.universe._
import org.dcs.api.service.RESTException
import org.dcs.api.model.ErrorConstants

object Reflector {

	def firstPrimaryConstructor[T: TypeTag](): MethodMirror = {
	  
			val m = ru.runtimeMirror(getClass.getClassLoader)
					val clazz = ru.typeOf[T].typeSymbol.asClass
					val cm = m.reflectClass(clazz)
					val primaryConstructorMSymbol = 
					ru.typeOf[T].decl(ru.termNames.CONSTRUCTOR).asTerm.alternatives.collectFirst {			  
					case ctor: MethodSymbol if ctor.isPrimaryConstructor => ctor
			}

			cm.reflectConstructor(primaryConstructorMSymbol.get)
	}
}