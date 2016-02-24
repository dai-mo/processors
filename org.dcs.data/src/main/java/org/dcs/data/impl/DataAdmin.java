package org.dcs.data.impl;

import java.util.List;
import java.util.UUID;

import org.dcs.api.model.DataSource;
import org.dcs.api.service.RESTException;

public interface DataAdmin {

	public UUID addDataSource(String dataSourceName, String dataSourceUrl) throws RESTException;

	public List<DataSource> getDataSources() throws RESTException;

}