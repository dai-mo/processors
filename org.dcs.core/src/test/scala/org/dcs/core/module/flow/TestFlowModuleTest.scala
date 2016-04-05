package org.dcs.core.module.flow

import java.util.HashMap
import org.dcs.api.service.ModuleFactoryService
import org.dcs.core.JUnitSpec
import org.dcs.core.api.service.impl.TestApiServiceImpl
import org.dcs.core.mock.MockBundleContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner
import org.osgi.framework.BundleContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties
import java.nio.charset.StandardCharsets

@RunWith(classOf[MockitoJUnitRunner])
class TestFlowModuleTest extends JUnitSpec {

  val logger = LoggerFactory.getLogger(classOf[TestFlowModuleTest])

  val className = "org.dcs.api.service.TestApiService"
  val serviceNameObjectMap = new HashMap[String, Object]()
  serviceNameObjectMap.put(className, new TestApiServiceImpl())

  @Spy
  var bundleContext: BundleContext = new MockBundleContext(serviceNameObjectMap);

  @InjectMocks
  var mFactory: ModuleFactoryService = new ModuleFactoryServiceImpl

  @Test
  def testLifeCycleOfTestFlowModuleInstance() {
    val moduleUUID: String =
      mFactory.createFlowModule("org.dcs.core.module.flow.TestFlowModule");
    
    moduleUUID should not be ()
    val module: FlowModule = mFactory.asInstanceOf[ModuleFactoryServiceImpl].getModule(moduleUUID);
    
    module should not be ()
    module shouldBe an[TestFlowModule]
    
    val user = "Bob";
    val valueProperties: Properties = new Properties();
    valueProperties.put(TestFlowModule.PROPERTY_USER_NAME_VALUE, user);

    val testResponse: String = 
      new String(mFactory.trigger(moduleUUID, valueProperties), StandardCharsets.UTF_8);
    
    testResponse should be("Hello " + user + "! This is DCS");

    mFactory.remove(moduleUUID);

    mFactory.asInstanceOf[ModuleFactoryServiceImpl].getModule(moduleUUID) should be (null)

  }
}