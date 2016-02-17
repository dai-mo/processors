package org.dcs.core.osgi;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.inject.Inject;

import org.dcs.api.service.DataApiService;
import org.dcs.api.service.ModulesApiService;
import org.dcs.data.config.ConfigurationFacade;
import org.dcs.test.paxe.PaxExamConfigOptionsFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cmathew on 18/01/16.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class OSGiBundleIT {

  static final Logger logger = LoggerFactory.getLogger(OSGiBundleIT.class);

  @Inject
  private ModulesApiService modulesService;

  @Inject
  private DataApiService dataService;

  @Configuration
  public Option[] config() {
  	return PaxExamConfigOptionsFactory.generateConfigOptions(this.getClass());
  }


  @Test
  public void testLaunchBundle() throws Exception {
  	assertSame(ConfigurationFacade.getInstance(), ConfigurationFacade.getInstance());
    assertNotNull(modulesService);
    assertNotNull(dataService);
  }
}
