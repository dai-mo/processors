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
            .substring(0, thisPath.indexOf(TARGET_TEST_CLASSES_MARKER) +
                    TARGET_TEST_CLASSES_MARKER.length())
            .replaceAll(TARGET_TEST_CLASSES_MARKER, "target/classes");
    logger.info("Bundle directory : "  + bundleDir);

    return options(
            mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.scr"),
            bundle("reference:file:" + bundleDir),
            systemPackages("org.dcs.api.service", "javax.ws.rs.core"),
            // add SLF4J and logback bundles
            mavenBundle("org.slf4j", "slf4j-api").startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            mavenBundle("ch.qos.logback", "logback-core").startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            mavenBundle("ch.qos.logback", "logback-classic").startLevel( START_LEVEL_SYSTEM_BUNDLES ),

            // Set logback configuration via system property.
            // This way, both the driver and the container use the same configuration
            systemProperty("logback.configurationFile").value( "file:" + PathUtils.getBaseDir() +
                    "/src/test/resources/logback-test.xml" ),
            systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
            junitBundles()
    );
  }




}
