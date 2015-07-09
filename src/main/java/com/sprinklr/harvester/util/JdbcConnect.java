package com.sprinklr.harvester.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JdbcConnect {

	public static PropertyHandler propHandler = new PropertyHandler();

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

			// System.out.println("Connecting to database....");

			// System.out.println("jdbc:mysql://"+host+":"+port+"/"+dbname+"," +
			// user+ ","+ pass);

			con = DriverManager.getConnection("jdbc:mysql://" + host + ":"
					+ port + "/" + dbname + utf8, user, pass);

			if (con != null) {
				System.out.println("Connected to database with URL "
						+ "jdbc:mysql://" + host + ":" + port + "/" + dbname);
			} else {
				System.out.println("Connection Failed!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * Method to read Urls from csv file & put them in List<String> Url
	 * 
	 * @return List<String> Url values from csv file
	 */
	public static List<String> csvParser() {
		CsvParser csv = new CsvParser();
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

			query = "SELECT ID FROM SOURCE WHERE NAME LIKE '%CTrip%'";
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
					System.out
							.println("Closing DB Connection...................");
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

		return sourceId;
	}

	/**
	 * Method to read data from Database by comparing urls from csv file, put
	 * them in the HashMap with Id as Key & Url as value.
	 * 
	 * @return HashMap<Integer, String>
	 */
	public static HashMap<Integer, String> getHashMappedDBData() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		HashMap<Integer, String> stubData = new HashMap<Integer, String>();
		List<String> UrlList = new ArrayList<String>();
		Integer count = 0;

		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			UrlList = csvParser();

			Iterator<String> urlListItr = UrlList.iterator();

			while (urlListItr.hasNext()) {
				String url = urlListItr.next();
				query = "SELECT ID, URL FROM HARVESTER WHERE URL='" + url + "'";
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					Integer id = rs.getInt("ID");
					String urlFromDB = rs.getString("URL");
					stubData.put(id, urlFromDB);
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
					System.out
							.println("Closing DB Connection...................");
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
		return stubData;
	}

	/**
	 * Method to read data from csv file with id & url, put them in the HashMap
	 * with Id as Key & Url as value.
	 * 
	 * @return HashMap<Integer, String>
	 */
	public static HashMap<Integer, String> getHashMappedFileData() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		HashMap<Integer, String> stubData = new HashMap<Integer, String>();
		List<String> UrlList = new ArrayList<String>();
		Integer count = 0;

		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			UrlList = csvParser();

			Iterator<String> urlListItr = UrlList.iterator();

			while (urlListItr.hasNext()) {
				String url = urlListItr.next();
				query = "SELECT ID, URL FROM HARVESTER WHERE URL='" + url + "'";
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					Integer id = rs.getInt("ID");
					String urlFromDB = rs.getString("URL");
					stubData.put(id, urlFromDB);
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
					System.out
							.println("Closing DB Connection...................");
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
			file = new File(
					"C:\\Users\\Rohan.Pandhare\\Desktop\\contentsFromDB.txt");
			FileWriter fw = new FileWriter(file);
			// PrintStream out = new PrintStream(System.out, false, "UTF8");

			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));

			conn = getDBConnection();
			stmt = conn.createStatement();
			query = "SELECT CONTENT FROM  DOCUMENT WHERE HARVESTER_ID = '1506408'";

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				// System.out.println(rs.getString("CONTENT").toString());
				// fw.append(rs.getString("CONTENT").toString());
				String ret = rs.getString("CONTENT").toString();
				System.out.println(ret);
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
					System.out
							.println("Closing DB Connection...................");
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

	/**
	 * Method to create csv file with stubid & url
	 */
	public static String createCsvDataFile() {
		String CommaDelimiter = ",";
		String NewLineSeparator = "\n";

		String fileLocation = System.getProperty("user.dir")
				+ "\\src\\main\\resources\\";
		// String fileLocation =
		// "C:\\Users\\Rohan.Pandhare\\Desktop\\Rohan\\CSVFiles";
		// String csvFileHeaders = "StubId,Url";

		FileWriter outCsvFile = null;

		Map<Integer, String> IdUrlMap = null;
		List<String> URLs = new ArrayList<String>();
		Set<Integer> IDs = new HashSet<Integer>();
		String OutFile = fileLocation + "\\csv"
				+ new Date().toString().replace(" ", "_").replace(":", "_")
				+ ".csv";

		try {

			IdUrlMap = getHashMappedDBData();
			Iterator<Integer> SItr = IdUrlMap.keySet().iterator();
			Iterator<String> LItr = IdUrlMap.values().iterator();

			System.out.println("Writing to file : " + OutFile);
			outCsvFile = new FileWriter(OutFile); // fileLocation+"\\csv"+ new
													// Date().toString()+".csv");

			// outCsvFile.append(csvFileHeaders.toString());
			// outCsvFile.append(NewLineSeparator.toString());

			while (SItr.hasNext()) {
				outCsvFile.append(Integer.toString(SItr.next()));
				outCsvFile.append(CommaDelimiter.toString());
				outCsvFile.append(LItr.next().toString());
				outCsvFile.append('\n');
			}

			System.out.println("Writing to file : " + OutFile
					+ " Done!!!!!!!!!");

			return OutFile;
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outCsvFile != null) {
				try {
					outCsvFile.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * Testing purposes only
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		getContent();
	}

}
