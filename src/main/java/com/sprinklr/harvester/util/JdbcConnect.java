package com.sprinklr.harvester.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.test.CTripTester;

/**
 * Reusable functions for accessing the DB data.
 *
 */
public class JdbcConnect {

	public final static Logger LOGGER = Logger.getLogger(CTripTester.class);
	public static PropertyHandler propHandler = new PropertyHandler();

	public static Connection getDBConnection() {
		Connection connection = null;
		String driver = PropertyHandler.getProperties().getProperty("driver");
		String host = PropertyHandler.getProperties().getProperty("host");
		String port = PropertyHandler.getProperties().getProperty("port");
		String user = PropertyHandler.getProperties().getProperty("user");
		String pass = PropertyHandler.getProperties().getProperty("pass");
		String dbname = PropertyHandler.getProperties().getProperty("dbname");
		String utf8 = "?useUnicode=true&characterEncoding=UTF-8";

		try {
			Class.forName(driver);

			LOGGER.info("getting database connection with " + "jdbc:mysql://" + host + ":" + port + "/" + dbname + ","
			        + user + "," + pass);
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname + utf8, user,
			        pass);
			if (connection != null) {
				LOGGER.info("Connected to database with URL " + "jdbc:mysql://" + host + ":" + port + "/" + dbname);
			} else {
				LOGGER.warn("Connection Failed!!!");
				// System.out.println("Connection Failed!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("returning connection instance");
		return connection;
	}

	/**
	 * Method to read URLs from CSV file & put them in List<String> URL
	 * 
	 * @return List<String> URL values from CSV file
	 */
	public static List<String> csvParser() {
		LOGGER.info("returning list of url from csvParser()");
		List<String> urlListFromCsvFile = new ArrayList<String>();
		urlListFromCsvFile = CsvParser.parseCsv();
		return urlListFromCsvFile;
	}

	/**
	 * Method which fetches source id of source provided in
	 * BulkRunner.properties file
	 * 
	 * @return Integer SourceId
	 */
	public static Integer getSourceId() {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		Integer sourceId = 0;

		String source = PropertyHandler.getProperties().getProperty("source");
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			LOGGER.info("getting sourceId from source table for " + source);
			query = "SELECT ID FROM SOURCE WHERE NAME LIKE '%" + source + "%'";
			LOGGER.info("Quering : " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				sourceId = rs.getInt("ID");
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
					LOGGER.info("closing database connection...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		LOGGER.info("returning extracted sourceId : " + sourceId);
		return sourceId;
	}

	/**
	 * Get the stub ID for the given URL.
	 * @param stubURL
	 * @return
	 */
	public static int getStubID(String stubURL) {
		LOGGER.debug("gettingStubId for url : " + stubURL);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();

			query = "SELECT ID, URL FROM HARVESTER WHERE URL='" + stubURL + "'";
			LOGGER.info("Quering : " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getInt("ID");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * Method to read data from Database by comparing urls from csv file, put
	 * them in the HashMap with Id as Key & Url as value.
	 * 
	 * @return HashMap<Integer, String>
	 */
	public static HashMap<Integer, InitialData> getHashMappedDBData() {
		LOGGER.info("getHashMappedDBData() getting database data & putting it into hashmap<Integer, InitialData>");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		HashMap<Integer, InitialData> stubData = new HashMap<Integer, InitialData>();

		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			csvParser();
			ArrayList<InitialData> list = CsvParser.getStubCSVData();

			for (int i = 0; i < list.size(); i++) {
				String url = list.get(i).getStubURL();
				query = "SELECT ID, URL FROM HARVESTER WHERE URL='" + url + "'";
				LOGGER.info("getHashMappedDBData() Quering : " + query);
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					int id = rs.getInt("ID");
					InitialData initialData = new InitialData();
					initialData.setStubEndpoint(list.get(i).getStubEndpoint());
					initialData.setStubURL(list.get(i).getStubURL());
					initialData.setStubID(id);
					stubData.put(id, initialData);
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
					LOGGER.info("getHashMappedDBData() Closing DB connection");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		LOGGER.info("getHashMappedDBData() returning database data wrapped into Hashmap<Integer, InitialData>");
		return stubData;
	}

	public static HashMap<Integer, InitialData> getHashMappedDBData(String url, String stubEndPoint) {
		LOGGER.info("getHashMappedDBData() getting database data & putting it into hashmap<Integer, InitialData>");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		HashMap<Integer, InitialData> stubData = new HashMap<Integer, InitialData>();

		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			query = "SELECT ID, URL FROM HARVESTER WHERE URL='" + url + "'";
			LOGGER.info("getHashMappedDBData() Quering : " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				int id = rs.getInt("ID");
				InitialData initialData = new InitialData();
				initialData.setStubEndpoint(stubEndPoint);
				initialData.setStubURL(url);
				initialData.setStubID(id);
				stubData.put(id, initialData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
					LOGGER.info("getHashMappedDBData() Closing DB connection");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		LOGGER.info("getHashMappedDBData() returning database data wrapped into Hashmap<Integer, InitialData>");
		return stubData;
	}
}
