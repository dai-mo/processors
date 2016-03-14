package org.dcs.core.services;

import org.dcs.api.service.RESTException;

import java.util.List;

/**
 * Created by laurent on 05/03/16.
 */
public interface DataSourcesService {
  String addDatasource(String sourceName, String uriStr) throws RESTException;

  List<String> getDataSources() throws RESTException;
}
