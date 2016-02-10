package org.dcs.api.data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.inject.Singleton;

import org.dcs.api.RESTException;
import org.dcs.api.model.DataSource;
import org.dcs.api.model.ErrorCode;
import org.dcs.config.ConfigurationFacade;
import org.dcs.config.DataConfiguration;

@Singleton
public class DataAdmin {
	
	
	private DataConfiguration dataConfiguration;
	private SQLDataSource sqlDataSource;
	
	public DataAdmin() throws RESTException {
		dataConfiguration = ConfigurationFacade.getCurrentDataConfiguration();		
		try {
			sqlDataSource = new SQLDataSource("org.sqlite.JDBC", dataConfiguration.getDataAdminDbPath());
			init();
		} catch (Exception e) {
			throw new RESTException(ErrorCode.DCS106(), e);
		} 
	}
	
	private void init() throws SQLException {
			String dataSourceCreateTable = "CREATE TABLE if not exists datasource (uuid TEXT PRIMARY KEY, name TEXT, url TEXT)";
			sqlDataSource.executeStmt(dataSourceCreateTable);	  
	}
	
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
			throw new RESTException(ErrorCode.DCS107(), sqle);
		}
		return uuid;
	}
	
	public DataSource getDataSource(UUID uuid) throws RESTException {
		String dataSourceQuery = "SELECT uuid, name, URL " +
				"FROM datasource WHERE uuid = ?";
		try (Connection conn = sqlDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(dataSourceQuery)) {
			pstmt.setString(1, uuid.toString());       
			ResultSet rs  = pstmt.executeQuery();
			while(rs.next()) {
				return new DataSource(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3));
			}
			throw new RESTException(ErrorCode.DCS107(), "No data source found for given uuid");
		} catch (SQLException e) {
			throw new RESTException(ErrorCode.DCS107(), e);
		}
	}
}
