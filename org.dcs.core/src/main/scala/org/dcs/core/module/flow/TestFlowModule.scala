package org.dcs.core.module.flow

import org.dcs.api.service.TestApiService
import java.util.Properties
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import org.dcs.api.service.FlowModule
import java.lang.Boolean
import org.dcs.api.model.TestResponse
import java.nio.charset.StandardCharsets
import scala.collection.JavaConverters._
import java.util.{ Map => JavaMap }

import java.util.HashMap

object TestFlowModule {
  val PropertyUserNameValue = "User Name";
}

class TestFlowModule extends FlowModule {

  var testService: TestApiService = _

  val propertiesMap = {
    val userNameProperties = collection.mutable.Map(
      FlowModule.PropertyName -> TestFlowModule.PropertyUserNameValue,
      FlowModule.PropertyDescription -> "User To Greet",
      FlowModule.PropertyRequired -> "false",
      FlowModule.PropertyDefaultValue -> "")

    collection.mutable.Map(TestFlowModule.PropertyUserNameValue -> userNameProperties.asJava).asJava
  }
  //    val userNameProperties = new HashMap[String, String]()
  //  userNameProperties.put(FlowModule.PropertyName, TestFlowModule.PropertyUserNameValue)
  //  userNameProperties.put(FlowModule.PropertyDescription, "User To Greet")
  //  userNameProperties.put(FlowModule.PropertyRequired, "false")
  //  userNameProperties.put(FlowModule.PropertyDefaultValue, "")
  //
  //  val propertiesMap = new HashMap[String, JavaMap[String, String]]()
  //  propertiesMap.put(TestFlowModule.PropertyUserNameValue, userNameProperties)

  val relationshipsMap = {
    val userNameRelationships = collection.mutable.Map(
      FlowModule.PropertyName -> FlowModule.RelSuccessId,
      FlowModule.PropertyDescription -> "All status updates will be routed to this relationship")

    collection.mutable.Map(FlowModule.RelSuccessId -> userNameRelationships.asJava).asJava
  }
  //    val userNameRelationships = new HashMap[String, String]()
  //  userNameRelationships.put(FlowModule.PropertyName, FlowModule.RelSuccessId)
  //  userNameRelationships.put(FlowModule.PropertyDescription, "All status updates will be routed to this relationship")
  //
  //  val relationshipsMap = new HashMap[String, JavaMap[String, String]]()
  //  relationshipsMap.put(FlowModule.RelSuccessId, userNameRelationships)

  override def init(bundleContext: BundleContext) {
    val className = classOf[TestApiService].getName
    val reference: ServiceReference[TestApiService] =
      bundleContext.getServiceReference[TestApiService](classOf[TestApiService])
    testService = bundleContext.getService(reference);

  }

  override def getPropertyDescriptors(): JavaMap[String, JavaMap[String, String]] = {
    propertiesMap;
  }

  override def getRelationships(): JavaMap[String, JavaMap[String, String]] = {
    relationshipsMap;
  }

  override def schedule() {

  }

  override def trigger(properties: JavaMap[String, String]): Array[Byte] = {
    val response: TestResponse = testService.testHelloGet(properties.get(TestFlowModule.PropertyUserNameValue));
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