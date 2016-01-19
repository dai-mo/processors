package org.dcs.osgi;

import org.dcs.api.service.ModulesApiService;
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

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * Created by cmathew on 18/01/16.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)

public class OSGiBundleTest extends OSGiTestBase {

  static final Logger logger = LoggerFactory.getLogger(OSGiBundleTest.class);

  @Inject
  private ModulesApiService modulesService;

  @Configuration
  public Option[] config() {
    return PaxExamConfigOptionsFactory.generateConfigOptions(this.getClass());
  }

  @Test
  public void testLaunchBundle() throws Exception {
    assertNotNull(modulesService);
  }
}
