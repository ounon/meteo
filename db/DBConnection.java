/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : DBConnection.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 12.04.2016
 Purpose     : Tools to establish a connection with the data base. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Class.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 * @date 12.04.2016
 * @version 1.0
 */
public class DBConnection {


	/**
	 * Constructor that needs the connection form to establish a connection.
	 * 
	 * @param connectionForm
	 * @throws SQLException
	 */
	public DBConnection (ConnectionForm connectionForm) throws SQLException{
		
		URL = "jdbc:mysql://" + connectionForm.getHostname()	   +":"
							  + connectionForm.getPort()		   +"/"
							  + connectionForm.getConnectionName() + "?user="
							  + connectionForm.getUsername() 	   + "&password="
							  + connectionForm.getPassword();
		
		connection = DriverManager.getConnection(URL);
		statement  = connection.createStatement();
	}


	
	/**
	 * This method execute an sql query and return the resultSet.
	 *
	 * @param sql
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery (String sql) throws SQLException {
		return statement.executeQuery(sql);
	}

	
	/**
	 * This method execute an sql wuery for an update.
	 *
	 * @param sql
	 * @return int
	 * @throws SQLException
	 */
	public int executeUpdate (String sql) throws SQLException {
		return statement.executeUpdate(sql);
	}
	

	/**
	 * Returns a prepared statement 
	 *
	 * @param sql
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement (String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}
	
	
	/**
	 * Executes an sql query. 
	 *
	 * @param sql
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean execute (String sql) throws SQLException {
		return statement.execute(sql);
	}
	
	
	/**
	 * Prepares an sql call.
	 *
	 * @param sql
	 * @return CallableStatement
	 * @throws SQLException
	 */
	public CallableStatement prepareCall (String sql) throws SQLException {
		return connection.prepareCall(sql);
	}

	
	/**
	 * Closes the connection.
	 */
	public void close() {
		
		try {
			
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	
	/** URL for the connection */
	private static String URL;
	/** The connection */
	private Connection connection;
	/** The statement */
	public  Statement  statement;
}
