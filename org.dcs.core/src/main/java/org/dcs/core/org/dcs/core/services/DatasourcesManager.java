package org.dcs.core.org.dcs.core.services;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.dcs.api.model.ErrorConstants;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by laurent on 03/03/16.
 */



public class DatasourcesManager {

  private static final Logger logger = LoggerFactory.getLogger(DatasourcesManager.class);

  public static final String ZOOKEEPER_CONFIG_STRING = "zookeeper.hosts";
  public static final String ZOOKEEPER_DATASOURCE_PATH = "zookeeper.datasources";
  public static final String CONFIG_ID = "org.dcs";

  @Inject
  @OsgiService
  private ConfigurationAdmin configAdmin;

  private RetryPolicy retryPolicy;
  private String connectionString;
  private String baseDatasourcesPath;

  public DatasourcesManager() throws RESTException {
    retryPolicy = new ExponentialBackoffRetry(1000, 3);
    connectionString = (String) configuration().getProperties().get(ZOOKEEPER_CONFIG_STRING);
    baseDatasourcesPath = (String) configuration().getProperties().get(ZOOKEEPER_DATASOURCE_PATH);
  }

  public DatasourcesManager addDatasource(String sourceName, String uriStr) throws RESTException {
    try (CuratorFramework fwk = CuratorFrameworkFactory.newClient(connectionString, retryPolicy)) {
      StringBuilder path = new StringBuilder(baseDatasourcesPath);
      path.append("/").append(sourceName);
      fwk.start();
      fwk.create().forPath(baseDatasourcesPath, uriStr.getBytes());
      logger.debug(new StringBuilder("Added datasource: ").append(sourceName).toString());
      return this;
    } catch (Exception e) {
      logger.error(new StringBuilder("Cannot add datasource: ").append(sourceName).toString());
      throw new RESTException(ErrorConstants.getErrorResponse("DCS101"), e);
    }
  }

  public List<String> getDatasources() throws RESTException {
    try ( CuratorFramework fwk = CuratorFrameworkFactory.newClient(connectionString, retryPolicy) )  {
      fwk.start();
      return fwk.getChildren().forPath(baseDatasourcesPath);
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
