package org.dcs.core.module.flow

import org.dcs.api.service.TestApiService
import java.util.Properties
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.dcs.api.service.FlowModuleConstants
import java.lang.Boolean
import org.dcs.api.model.TestResponse
import java.nio.charset.StandardCharsets
import java.util.HashMap

object TestFlowModule {
  val PROPERTY_USER_NAME_VALUE = "User Name";
}

class TestFlowModule() extends FlowModule {

  var testService: TestApiService = _
  var propertiesMap = new HashMap[String, Properties]()
  var relationshipsMap = new HashMap[String, Properties]()

  override def init(bundleContext: BundleContext) {
    val className = classOf[TestApiService].getName
    val reference: ServiceReference[TestApiService] =
      bundleContext.getServiceReference[TestApiService](classOf[TestApiService])
    testService = bundleContext.getService(reference);

    var userNameProperties: Properties = new Properties()
    userNameProperties.put(FlowModuleConstants.PROPERTY_NAME, TestFlowModule.PROPERTY_USER_NAME_VALUE)
    userNameProperties.put(FlowModuleConstants.PROPERTY_DESCRIPTION, "User To Greet")
    userNameProperties.put(FlowModuleConstants.PROPERTY_REQUIRED, new Boolean(false))
    userNameProperties.put(FlowModuleConstants.PROPERTY_DEFAULT_VALUE, "")

    propertiesMap.put(TestFlowModule.PROPERTY_USER_NAME_VALUE, userNameProperties)

    var userNameRelationships: Properties = new Properties()
    userNameRelationships.put(FlowModuleConstants.PROPERTY_NAME, FlowModuleConstants.REL_SUCCESS_ID);
    userNameRelationships.put(FlowModuleConstants.PROPERTY_DESCRIPTION, "All status updates will be routed to this relationship");

    relationshipsMap.put(FlowModuleConstants.REL_SUCCESS_ID, userNameRelationships);
  }

  override def getPropertyDescriptors(): java.util.Map[String, Properties] = {
    propertiesMap;
  }

  override def getRelationships(): java.util.Map[String, Properties] = {
    relationshipsMap;
  }

  override def schedule() {

  }

  override def trigger(properties: Properties): Array[Byte] = {
    val response: TestResponse = testService.testHelloGet(properties.getProperty(TestFlowModule.PROPERTY_USER_NAME_VALUE));
    response.getResponse().getBytes(StandardCharsets.UTF_8);
  }

  override def unschedule() {

  }

  override def stop() {

  }

  override def shutdown() {

  }

  override def remove() {

  }

}