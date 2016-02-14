package org.dcs.core.data.impl;

import java.util.List;
import java.util.UUID;

import org.dcs.api.RESTException;
import org.dcs.api.model.DataSource;

public interface DataAdmin {

	public UUID addDataSource(String dataSourceName, String dataSourceUrl) throws RESTException;

	public List<DataSource> getDataSources() throws RESTException;

}