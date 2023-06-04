package com.automation.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * JDBC class to connect to the database
 * @author reazur.rahman
 *
 */


public class ApacheJDBC {
	


	public Connection con = null;
	public Statement stmt = null;
	public ResultSet rs = null;

	
	/**
	 * Getter method for connection
	 * 
	 * @return the connection
	 */
	public Connection getCon() {
		return con;
	}
	
	/**
	 * 
	 * Setter Method for connection
	 * @param con Sets the connection
	 */

	public void setCon(Connection con) {
		this.con = con;
	}
	
	/** 
	 *  Getter method for statement to execute in SQL
	 * @return returns the statement
	 */

	public Statement getStmt() {
		return stmt;
	}
	
	/**
	 * 
	 * Setter Method for statement
	 * @param stmt The statement to execute
	 */

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	
	/**
	 * 
	 * Getter method for the ResultSet
	 * @return returns the result set
	 */
	
	public ResultSet getRs() {
		return rs;
	}
	
	/**
	 * 
	 * Setter method for Result Set
	 * @param rs Set the result set
	 */

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	
	/**
	 * This method connects to the data. It takes in two parameter dataBaseName and portNumber
	 * 
	 * @param dataBaseName Data Base name to connect to 
	 * @param portNumber Port number to connect to
	 */

	public void ConnectToDataBase(String dataBaseName, String portNumber) {

		String connectionUrl = "jdbc:sqlserver://" + dataBaseName + ":" + portNumber + ";"
				+ "databaseName=databaseName;integratedSecurity=true;";

		System.out.println("Connection URl is: " + connectionUrl);

		// Declare the JDBC objects.

		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);

			System.out.println("In the try block");

			// Create and execute an SQL statement that returns some data.
			String SQL = "SELECT * FROM table_name";
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			// Iterate through the data in the result set and display it.
			System.out.println("AgeGroupKey\t" + "AgeGroupType");
			while (rs.next()) {
				System.out.println(rs.getString("AgeGroupKey") + "\t        " + rs.getString("AgeGroupType"));
			}

		}

		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			System.out.println("Out of the block");
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}

}