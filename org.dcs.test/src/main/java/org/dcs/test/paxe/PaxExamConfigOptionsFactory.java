package org.dcs.test.paxe;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Created by cmathew on 19/01/16.
 */
public class PaxExamConfigOptionsFactory {
  private static final Logger logger = LoggerFactory.getLogger(PaxExamConfigOptionsFactory.class);

  private static String TARGET_TEST_CLASSES_MARKER = "target/test-classes";


  public static Option[] generateConfigOptions(Class testClass) {

    URL thisUrl = testClass.getResource(".");
    File thisDir = new File(thisUrl.getFile());
    String thisPath = thisDir.getAbsolutePath();

    String bundleDir = thisPath
            .substring(0, thisPath.lastIndexOf(TARGET_TEST_CLASSES_MARKER) +
                    TARGET_TEST_CLASSES_MARKER.length())
            .replaceAll(TARGET_TEST_CLASSES_MARKER, "target/classes");
    logger.info("Bundle directory : "  + bundleDir);

    return options(
            mavenBundle().groupId("javax.annotation").artifactId("javax.annotation-api"),
            mavenBundle().groupId("javax.interceptor").artifactId("javax.interceptor-api"),
            mavenBundle().groupId("javax.el").artifactId("javax.el-api"),
            mavenBundle().groupId("javax.enterprise").artifactId("cdi-api").version("1.2"),
            mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.scr"),
            mavenBundle().groupId("org.ops4j.pax.cdi").artifactId("pax-cdi-api").version("0.12.0"),
            mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils"),
            mavenBundle().groupId("org.ops4j.pax.cdi").artifactId("pax-cdi-spi").version("0.12.0"),
            mavenBundle().groupId("org.ops4j.pax.cdi").artifactId("pax-cdi-extension").version("0.12.0"),
            mavenBundle().groupId("org.ops4j.pax.cdi").artifactId("pax-cdi-extender").version("0.12.0"),


            mavenBundle().groupId("com.google.guava").artifactId("guava").version("19.0"),
            mavenBundle().groupId("org.jboss.classfilewriter").artifactId("jboss-classfilewriter").version("1.1.2.Final"),
            mavenBundle().groupId("org.jboss.logging").artifactId("jboss-logging").version("3.3.0.Final"),
            mavenBundle().groupId("org.jboss.weld").artifactId("weld-osgi-bundle").version("2.3.2.Final"),
            mavenBundle().groupId("org.ops4j.pax.cdi").artifactId("pax-cdi-weld").version("0.12.0"),

            mavenBundle().groupId("org.glassfish.jersey.bundles.repackaged").artifactId("jersey-guava").version("2.22.1"),
            mavenBundle().groupId("javax.servlet").artifactId("javax.servlet-api").version("3.1.0"),
            mavenBundle().groupId("org.glassfish.hk2.external").artifactId("aopalliance-repackaged").version("2.4.0"),
            mavenBundle().groupId("org.glassfish.hk2").artifactId("hk2-utils").version("2.4.0"),
            mavenBundle().groupId("org.glassfish.hk2").artifactId("osgi-resource-locator").version("1.0.1"),
            //mavenBundle().groupId("aopalliance").artifactId("aopalliance").version("1.0"),
            mavenBundle().groupId("org.glassfish.hk2").artifactId("hk2-api").version("2.4.0"),
            mavenBundle().groupId("org.javassist").artifactId("javassist").version("3.18.1-GA"),

            mavenBundle().groupId("org.glassfish.hk2").artifactId("hk2-locator").version("2.4.0"),
            mavenBundle().groupId("javax.validation").artifactId("validation-api").version("1.1.0.Final"),
            mavenBundle().groupId("org.glassfish.jersey.core").artifactId("jersey-client").version("2.22.1"),

            mavenBundle().groupId("org.glassfish.jersey.core").artifactId("jersey-server").version("2.22.1"),
            mavenBundle().groupId("org.glassfish.jersey.core").artifactId("jersey-common").version("2.22.1"),
            mavenBundle().groupId("org.glassfish.jersey.containers").artifactId("jersey-container-servlet-core").version("2.22.1"),
            mavenBundle().groupId("org.glassfish.jersey.containers").artifactId("jersey-container-servlet").version("2.22.1"),

            mavenBundle().groupId("org.jvnet.mimepull").artifactId("mimepull").version("1.9.6"),
            mavenBundle().groupId("org.glassfish.jersey.media").artifactId("jersey-media-multipart").version("2.22.1"),
            mavenBundle().groupId("javax.ws.rs").artifactId("javax.ws.rs-api").version("2.0.1"),

            systemPackages("org.dcs.api.service", "org.dcs.api.data", "org.dcs.api", "org.dcs.api.model", "javax.ws.rs.core", "org.ops4j.pax.cdi.extension"),
            // add SLF4J and logback bundles
            mavenBundle("org.slf4j", "slf4j-api").startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            mavenBundle("ch.qos.logback", "logback-core").startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            mavenBundle("ch.qos.logback", "logback-classic").startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            bundle("reference:file:" + bundleDir),
            // Set logback configuration via system property.
            // This way, both the driver and the container use the same configuration
            systemProperty("logback.configurationFile").value( "file:" + PathUtils.getBaseDir() +
                    "/src/test/resources/logback-test.xml" ),
            systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
            junitBundles()
    );
  }




}
