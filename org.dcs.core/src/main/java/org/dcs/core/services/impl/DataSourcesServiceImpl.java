package org.dcs.core.services.impl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.dcs.api.model.ErrorConstants;
import org.dcs.api.service.RESTException;
import org.dcs.core.services.DataSourcesService;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by laurent on 03/03/16.
 */


@OsgiServiceProvider
@OsgiService
@Properties({
    @Property(name = "service.exported.interfaces", value = "*"),
    @Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class DataSourcesServiceImpl implements DataSourcesService {

  private static final Logger logger = LoggerFactory.getLogger(DataSourcesServiceImpl.class);

  public static final String ZOOKEEPER_CONFIG_STRING = "zookeeper.hosts";
  public static final String ZOOKEEPER_DATASOURCE_PATH = "zookeeper.datasources";
  public static final String CONFIG_ID = "org.dcs";

  @Inject
  @OsgiService
  private ConfigurationAdmin configAdmin;

  private RetryPolicy retryPolicy;
  private String connectionString;
  private String dataSourcesBasePath;

  @PostConstruct
  private final void init() throws RESTException {
  	retryPolicy = new ExponentialBackoffRetry(1000, 3);
    connectionString = (String) configuration().getProperties().get(ZOOKEEPER_CONFIG_STRING);
    dataSourcesBasePath = (String) configuration().getProperties().get(ZOOKEEPER_DATASOURCE_PATH);
//    try {
//    } catch (RESTException e) {
//      e.printStackTrace();
//    }
  }


  @Override
  public String addDatasource(String sourceName, String uriStr) throws RESTException {
    try (CuratorFramework fwk = CuratorFrameworkFactory.newClient(connectionString, retryPolicy)) {
      StringBuilder path = new StringBuilder(dataSourcesBasePath);
      path.append("/").append(sourceName);
      fwk.start();
      fwk.create().forPath(dataSourcesBasePath, uriStr.getBytes());
      logger.debug(new StringBuilder("Added datasource: ").append(sourceName).toString());
      return uriStr;
    } catch (Exception e) {
      logger.error(new StringBuilder("Cannot add datasource: ").append(sourceName).toString());
      throw new RESTException(ErrorConstants.getErrorResponse("DCS101"), e);
    }
  }

  @Override
  public List<String> getDataSources() throws RESTException {
    try ( CuratorFramework fwk = CuratorFrameworkFactory.newClient(connectionString, retryPolicy) )  {
      fwk.start();
      return fwk.getChildren().forPath(dataSourcesBasePath);
    } catch (Exception e) {
      throw new RESTException(ErrorConstants.getErrorResponse("DCS101"), e);
    }
  }

  // ###############################################################################

  private Configuration configuration() throws RESTException {
    try {
      return configAdmin.getConfiguration(CONFIG_ID);
    } catch (IOException e) {
      logger.error(new StringBuilder("Cannot get configuration for ").append(CONFIG_ID).toString());
      throw new RESTException(ErrorConstants.getErrorResponse("DCS101"), e);
    }
  }


}
