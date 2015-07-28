package com.sprinklr.harvester.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.seleniumhq.jetty7.security.JDBCLoginService;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.test.testCtrip;

public class JdbcConnect {

	public static PropertyHandler propHandler = new PropertyHandler();
	public static Logger JdbcLogger = testCtrip.logger;
	
	public static Connection getDBConnection() {
		
		
		Connection con = null;
		String driver = PropertyHandler.getProperties().getProperty("driver");
		String host = PropertyHandler.getProperties().getProperty("host");
		String port = PropertyHandler.getProperties().getProperty("port");
		String user = PropertyHandler.getProperties().getProperty("user");
		String pass = PropertyHandler.getProperties().getProperty("pass");
		String dbname = PropertyHandler.getProperties().getProperty("dbname");
		String utf8 = "?useUnicode=true&characterEncoding=UTF-8";
		
		try {
			Class.forName(driver);

			//System.out.println("Connecting to database....");

			//System.out.println("jdbc:mysql://" + host + ":" + port + "/"+ dbname + "," + user + "," + pass);
			
			JdbcLogger.info("getting database connection with " + "jdbc:mysql://" + host + ":" + port + "/"+ dbname + "," + user + "," + pass);
			
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":"+ port + "/" + dbname + utf8, user, pass);

			if (con != null) {
				//System.out.println("Connected to database with URL "+ "jdbc:mysql://" + host + ":" + port + "/" + dbname);
				JdbcLogger.info("Connected to database with URL "+ "jdbc:mysql://" + host + ":" + port + "/" + dbname);
			} else {
				JdbcLogger.warn("Connection Failed!!!");
				//System.out.println("Connection Failed!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JdbcLogger.info("returning connection instance");
		return con;
	}

	/**
	 * Method to read Urls from csv file & put them in List<String> Url
	 * 
	 * @return List<String> Url values from csv file
	 */
	public static List<String> csvParser() {
		JdbcLogger.info("returning list of url from csvParser()");
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
			JdbcLogger.info("getting sourceId from source table for " + source);
			query = "SELECT ID FROM SOURCE WHERE NAME LIKE '%" + source + "%'";
			JdbcLogger.info("Quering : "+ query);
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
					JdbcLogger.info("closing database connection...");
					//System.out.println("Closing DB Connection...................");
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
		JdbcLogger.info("returning extracted sourceId : " + sourceId);
		return sourceId;
	}

	public static int getStubID(String stubURL) {
		JdbcLogger.debug("gettingStubId for url : " + stubURL);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();

			query = "SELECT ID, URL FROM HARVESTER WHERE URL='" + stubURL + "'";
			JdbcLogger.info("Quering : " + query);
			//System.out.println("Query: " + query);

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
		JdbcLogger.info("getHashMappedDBData() getting database data & putting it into hashmap<Integer, InitialData>");
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
				//System.out.println("Query: " + query);
				JdbcLogger.info("getHashMappedDBData() Quering : " + query);
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
					/*System.out
							.println("Closing DB Connection...................");*/
					JdbcLogger.info("getHashMappedDBData() Closing DB connection");
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
		JdbcLogger.info("getHashMappedDBData() returning database data wrapped into Hashmap<Integer, InitialData>");
		return stubData;
	}

	/**
	 * Method to get content from nba.DOCUMENT table
	 */
	public static void getContent() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;

		File file = null;

		Writer out = null;

		try {
			file = new File("C:\\Users\\Rohan.Pandhare\\Desktop\\contentsFromDB.txt");
			JdbcLogger.info("getContent() getting content from file " + file);
			FileWriter fw = new FileWriter(file);
			// PrintStream out = new PrintStream(System.out, false, "UTF8");

			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));

			conn = getDBConnection();
			stmt = conn.createStatement();
			query = "SELECT CONTENT FROM  DOCUMENT WHERE HARVESTER_ID = '1506408'";
			JdbcLogger.info("Quering : " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String ret = rs.getString("CONTENT").toString();
				//System.out.println(ret);
				out.write(ret);
			}
			out.flush();
			out.close();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
					/*System.out
							.println("Closing DB Connection...................");*/
					JdbcLogger.info("getContent() closing database connection...");
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
	}

}
