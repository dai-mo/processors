package org.dcs.core.org.dcs.core.services;

import org.dcs.api.service.RESTException;
import org.dcs.core.org.dcs.core.services.impl.DataSourcesServiceImpl;

import java.util.List;

/**
 * Created by laurent on 05/03/16.
 */
public interface DataSourcesService {
  DataSourcesServiceImpl addDatasource(String sourceName, String uriStr) throws RESTException;

  List<String> getDataSources() throws RESTException;
}
