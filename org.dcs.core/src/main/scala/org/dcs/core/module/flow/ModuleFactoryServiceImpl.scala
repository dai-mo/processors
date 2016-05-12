package org.dcs.core.module.flow

import javax.enterprise.inject.Default
import org.ops4j.pax.cdi.api.OsgiService
import org.ops4j.pax.cdi.api.OsgiServiceProvider
import org.ops4j.pax.cdi.api.Properties
import org.ops4j.pax.cdi.api.Property
import org.dcs.api.service.ModuleFactoryService
import org.osgi.framework.BundleContext
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import scala.collection.immutable.HashMap
import javax.inject.Inject
import java.util.UUID
import org.dcs.api.service.RESTException
import org.dcs.api.model.ErrorConstants

import scala.collection.JavaConverters._
import java.util.{Map => JavaMap}
import scala.collection.mutable.{Map => MutableMap}

@OsgiServiceProvider
@OsgiService
@Properties(Array(
  new Property(name = "service.exported.interfaces", value = "*"),
  new Property(name = "service.exported.configs", value = "org.apache.cxf.ws")))
@Default
class ModuleFactoryServiceImpl extends ModuleFactoryService {

  val logger: Logger = LoggerFactory.getLogger(classOf[ModuleFactoryServiceImpl])

  var flowModuleMap: Map[String, FlowModule] = Map()

  @Inject
  var bundleContext: BundleContext = _

  override def createFlowModule(className: String): String = {

    val randomUUID: String = UUID.randomUUID().toString()
    val clazz = Class.forName(className)
    val ref = clazz.newInstance

    ref match {
      case ref: FlowModule => {
        ref.init(bundleContext);
        flowModuleMap = flowModuleMap + (randomUUID -> ref);
      }
      case s: String => {
        logger.warn("Given classname " + className + " is not of type FlowModule");
        throw new RESTException(ErrorConstants.getErrorResponse("DCS001"));
      }
    }
    randomUUID;
  }

  def getModule(moduleUUID: String): FlowModule = {
    flowModuleMap.getOrElse(moduleUUID, null)
  }

  override def getPropertyDescriptors(moduleUUID: String): JavaMap[String, JavaMap[String, String]] = {
    getModule(moduleUUID).getPropertyDescriptors();
  }

  override def getRelationships(moduleUUID: String): JavaMap[String, JavaMap[String, String]] = {
    getModule(moduleUUID).getRelationships();
  }

  override def schedule(moduleUUID: String): Boolean = {
    getModule(moduleUUID).schedule();
    true;
  }

  override def trigger(moduleUUID: String, properties: JavaMap[String, String]): Array[Byte] = {
    getModule(moduleUUID).trigger(properties);
  }

  override def unschedule(moduleUUID: String): Boolean = {
    getModule(moduleUUID).unschedule();
    true;
  }

  override def stop(moduleUUID: String): Boolean = {
    getModule(moduleUUID).stop();
    true;
  }

  override def shutdown(moduleUUID: String): Boolean = {
    getModule(moduleUUID).shutdown();
    true;
  }

  override def remove(moduleUUID: String): Boolean = {
    try {
      getModule(moduleUUID).remove();
    } finally {
      flowModuleMap -= (moduleUUID);
    }
    return true;
  }
}
