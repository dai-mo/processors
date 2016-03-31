package org.dcs.core.osgi;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import java.io.File;
import java.net.MalformedURLException;

import javax.inject.Inject;

import org.dcs.api.service.DataApiService;
import org.dcs.api.service.ModuleFactoryService;
import org.dcs.api.service.TestApiService;
import org.dcs.core.services.DataSourcesService;
import org.dcs.data.FileDataManager;
import org.dcs.data.impl.DataAdmin;
import org.dcs.test.DataUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionConfigurationFileReplacementOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExam.class)
public class CoreBundleLoadOsgiT {

	static final Logger logger = LoggerFactory.getLogger(CoreBundleLoadOsgiT.class);


	@Inject
	private ModuleFactoryService moduleFactoryService;

	@Inject
	private DataSourcesService dataSourcesService;

	@Inject
	private TestApiService testService;

	@Inject
	private DataApiService dataApiService;

	@Inject
	private FileDataManager fileDataManager;

	@Inject
	private DataAdmin dataAdmin;

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
		
		MavenUrlReference orgDcsFeaturesRepo = maven()
				.groupId("org.dcs")
				.artifactId("org.dcs.features")
				.versionAsInProject()
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
				
				features(karafEnterpriseRepo , "pax-cdi", "pax-cdi-weld", "scr", "wrap"),
				features(orgDcsFeaturesRepo , "org.dcs.features"), 

				mavenBundle("org.dcs","org.dcs.api").versionAsInProject().start(),
				mavenBundle("org.dcs","org.dcs.data").versionAsInProject().start(),
				mavenBundle("org.dcs","org.dcs.core").versionAsInProject(),
				CoreOptions.systemProperty("config").value(DataUtils.getKarafConfigurationFilePath(this.getClass())),				
				new KarafDistributionConfigurationFileReplacementOption("etc/org.dcs.cfg", 
						new File(DataUtils.getTargetTestClassesDirectory(this.getClass()) + File.separator + "org.dcs.cfg"))
		};
	}

	public static String karafVersion() {
		ConfigurationManager cm = new ConfigurationManager();      
		String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.0.4");      
		return karafVersion;
	}


	@Test
	public void testLaunchBundle() throws Exception {

		assertNotNull(fileDataManager);
		assertNotNull(dataAdmin);
		assertNotNull(dataApiService);
		assertNotNull(dataSourcesService);
		assertNotNull(testService);
		assertNotNull(moduleFactoryService);
	}
}
