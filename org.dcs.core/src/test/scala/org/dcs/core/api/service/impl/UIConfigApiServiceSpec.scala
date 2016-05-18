package org.dcs.core.api.service.impl

import org.dcs.core.JUnitSpec
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import org.slf4j.LoggerFactory
import org.dcs.test.mock.MockConfigurationAdmin
import org.mockito.Spy
import org.junit.Test
import org.osgi.service.cm.ConfigurationAdmin
import org.mockito.InjectMocks

@RunWith(classOf[MockitoJUnitRunner])
class UIConfigApiServiceSpec extends JUnitSpec {
  val logger = LoggerFactory.getLogger(classOf[UIConfigApiServiceSpec])
  
  @Spy
  var configAdmin: ConfigurationAdmin = new MockConfigurationAdmin
  
  @InjectMocks
  var uiConfigApiService = new UIConfigApiServiceImpl
  
  @Test
  def testUIConfig() {
    uiConfigApiService.uiConfigGet.getNifiUrl should be ("http://localhost:8080")
  }
  
}