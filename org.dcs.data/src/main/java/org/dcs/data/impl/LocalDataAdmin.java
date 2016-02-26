package org.dcs.data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.inject.Default;

import org.dcs.api.model.DataSource;
import org.dcs.api.model.ErrorConstants;
import org.dcs.api.service.RESTException;
import org.dcs.data.SQLiteDataConnector;
import org.dcs.data.config.ConfigurationFacade;
import org.dcs.data.config.DataConfiguration;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@OsgiServiceProvider
@Default
public class LocalDataAdmin implements DataAdmin {
	
	
	private DataConfiguration dataConfiguration;
	private SQLiteDataConnector sqlDataSource;
	
	public LocalDataAdmin() throws RESTException {
		dataConfiguration = ConfigurationFacade.getCurrentDataConfiguration();		
		try {
			sqlDataSource = new SQLiteDataConnector(dataConfiguration.getDataAdminDbPath());
			init();
		} catch (Exception e) {
			throw new RESTException(ErrorConstants.DCS106(), e);
		} 
	}
	
	private void init() throws SQLException {
			String dataSourceCreateTable = "CREATE TABLE if not exists datasource (uuid TEXT PRIMARY KEY, name TEXT, url TEXT)";
			sqlDataSource.executeStmt(dataSourceCreateTable);	  
	}
	
	/* (non-Javadoc)
	 * @see org.dcs.api.data.impl.DataAdmin#addDataSource(java.lang.String, java.lang.String)
	 */
	@Override
	public UUID addDataSource(String dataSourceName, String dataSourceUrl) throws RESTException  {
		UUID uuid = UUID.randomUUID();
		
		String dataSourceInsert = "INSERT INTO datasource(uuid, name, url) VALUES(?,?,?)";
		try (Connection conn = sqlDataSource.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(dataSourceInsert)) {
			pstmt.setString(1, uuid.toString());
			pstmt.setString(2, dataSourceName);
			pstmt.setString(3, dataSourceUrl);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			throw new RESTException(ErrorConstants.DCS107(), sqle);
		}
		return uuid;
	}
	
	/* (non-Javadoc)
	 * @see org.dcs.api.data.impl.DataAdmin#getDataSources()
	 */
	@Override
	public List<DataSource> getDataSources() throws RESTException {
		String dataSourceQuery = "SELECT uuid, name, URL  FROM datasource";
		try (Connection conn = sqlDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(dataSourceQuery)) {			
			ResultSet rs  = pstmt.executeQuery();
			List<DataSource> dataSources = new ArrayList<>();
			
			while(rs.next()) {
				DataSource ds = new DataSource();
				ds.setUuid(rs.getString(1));
				ds.setName(rs.getString(2));
				dataSources.add(ds);
			}
			return dataSources;
		} catch (SQLException e) {
			throw new RESTException(ErrorConstants.DCS107(), e);
		}
	}
}
