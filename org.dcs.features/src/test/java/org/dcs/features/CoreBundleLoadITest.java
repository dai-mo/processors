package org.dcs.features;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import java.io.File;
import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.dcs.api.service.DataApiService;
import org.dcs.api.service.ModulesApiService;
import org.dcs.test.DataUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExam.class)
public class CoreBundleLoadITest {

  static final Logger logger = LoggerFactory.getLogger(CoreBundleLoadITest.class);

  @Inject
  private ModulesApiService modulesService;

  @Inject
  private DataApiService dataService;
  
  @OsgiService
  DataSource dataSource;

  @Configuration
  public Option[] config() throws MalformedURLException {
      MavenArtifactUrlReference karafUrl = maven()
          .groupId("org.apache.karaf")
          .artifactId("apache-karaf")
          .version(karafVersion())
          .type("zip");

      MavenUrlReference karafEnterpriseRepo = maven()
          .groupId("org.apache.karaf.features")
          .artifactId("enterprise")
          .version(karafVersion())
          .classifier("features")
          .type("xml");
      return new Option[] {
          // KarafDistributionOption.debugConfiguration("5005", true),
          karafDistributionConfiguration()
              .frameworkUrl(karafUrl)                     
              .unpackDirectory(new File("target", "exam"))
              .useDeployFolder(false),
          keepRuntimeFolder(),
          configureConsole().ignoreLocalConsole(),
          features(DataUtils.getTargetDirectoryUrl(this.getClass()) + "/feature/feature.xml", "org.dcs.features"),
          features(karafEnterpriseRepo , "jdbc", "pax-jdbc", "pax-jdbc-config", "pax-jdbc-sqlite",
          		"pax-cdi", "pax-cdi-weld", "scr", "wrap"),
          CoreOptions.systemProperty("config").value(DataUtils.getTargetDirectory(this.getClass()) + 
          		"/test-classes/config.yaml")
     };
  }

  public static String karafVersion() {
      ConfigurationManager cm = new ConfigurationManager();      
      String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.0.4");
      logger.warn("karafVersion : " + karafVersion);
      return karafVersion;
  }


  @Test
  public void testLaunchBundle() throws Exception {
  	//assertSame(ConfigurationFacade.getInstance(), ConfigurationFacade.getInstance());
    assertNotNull(modulesService);
    assertNotNull(dataService);
  }
}
