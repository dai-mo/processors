package org.dcs.data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SQLDataSource {

	private static final String JDBC_SQLITE = "jdbc:sqlite:";
	
	int timeout = 30;
  public  String dbPath;


  /* quick and dirty constructor to test the database passing the DriverManager name and the fully loaded url to handle */
  /* NB this will typically be available if you make this class concrete and not abstract */
  public SQLDataSource(String jdbcDriverName, String dbPath) throws Exception {
  	this.dbPath = dbPath;
    Class.forName(jdbcDriverName);
   
  }
  
  public Connection getConnection() throws SQLException {
  	return DriverManager.getConnection(JDBC_SQLITE + dbPath);
  }
  
  public Statement getStatement(Connection connection) throws SQLException {
  	Statement statement = connection.createStatement();
    statement.setQueryTimeout(timeout); 
    return statement;
  }

  public  void executeStmt(String instruction) throws SQLException {
  	try(Connection connection = getConnection()) {
  		getStatement(connection).executeUpdate(instruction);
  	}
  }

  public void executeStmt(List<String> instructions) throws SQLException {
      for (String instruction : instructions) {
          executeStmt(instruction);
      }
  }

  public ResultSet executeQuery(String instruction) throws SQLException {
  	try(Connection connection = getConnection()) {
      return getStatement(connection).executeQuery(instruction);
  	}
  } 

}
