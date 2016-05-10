package org.dcs.core.module.flow

import org.dcs.api.service.TestApiService
import java.util.Properties
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.dcs.api.service.FlowModule
import java.lang.Boolean
import org.dcs.api.model.TestResponse
import java.nio.charset.StandardCharsets


object TestFlowModule {
  val PropertyUserNameValue = "User Name";
}

class TestFlowModule extends FlowModule {

  var testService: TestApiService = _
  val propertiesMap = {
    val userNameProperties = Map(FlowModule.PropertyName -> TestFlowModule.PropertyUserNameValue,
    FlowModule.PropertyDescription -> "User To Greet",
    FlowModule.PropertyRequired -> "false",
    FlowModule.PropertyDefaultValue -> "")
    
    Map(TestFlowModule.PropertyUserNameValue -> userNameProperties)
  }
  
  val relationshipsMap = {    
    val userNameRelationships = Map(FlowModule.PropertyName -> FlowModule.RelSuccessId,
    FlowModule.PropertyDescription -> "All status updates will be routed to this relationship")

    Map(FlowModule.RelSuccessId -> userNameRelationships)
  }

  override def init(bundleContext: BundleContext) {
    val className = classOf[TestApiService].getName
    val reference: ServiceReference[TestApiService] =
      bundleContext.getServiceReference[TestApiService](classOf[TestApiService])
    testService = bundleContext.getService(reference);


  }

  override def getPropertyDescriptors(): Map[String, Map[String, String]] = {
    propertiesMap;
  }

  override def getRelationships(): Map[String, Map[String, String]] = {
    relationshipsMap;
  }

  override def schedule() {

  }

  override def trigger(properties: Map[String, String]): Array[Byte] = {
    val response: TestResponse = testService.testHelloGet(properties(TestFlowModule.PropertyUserNameValue));
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