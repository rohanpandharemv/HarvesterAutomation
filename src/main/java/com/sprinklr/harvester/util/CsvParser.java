package com.sprinklr.harvester.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.seleniumhq.jetty7.security.JDBCLoginService;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.test.testCtrip;

/**
 * Class to parse csv file
 * 
 * @author Rohan.Pandhare
 *
 */
public class CsvParser {
	
	public static final Logger csvLogger = testCtrip.logger;
	private static ArrayList<InitialData> list = new ArrayList<InitialData>();

	public static ArrayList<InitialData> getStubCSVData() {
		csvLogger.info("CsvParser.getStubCSVData() returning list InitialData objects");
		return list;
	}

	/**
	 * Method to read & parse csv file,
	 * 
	 * @return List<String> List of all entries in csv file
	 */
	public static List<String> parseCsv() {
		
		list = new ArrayList<InitialData>();
		String source = PropertyHandler.getProperties().getProperty("source").toLowerCase();
		String csvFileToParse = System.getProperty("user.dir")
				+ "\\src\\main\\resources\\" + source + "\\" + source + ".csv";
		csvLogger.info("CsvParser.parseCsv() parsing csv file : "+ csvFileToParse);
		BufferedReader br = null;
		String line = "";
		List<String> linesList = new ArrayList<String>();

		try {
			br = new BufferedReader(new FileReader(csvFileToParse));

			while ((line = br.readLine()) != null) {
				System.out.println("---- ");
				linesList.add(line.split(",")[0]);
				InitialData e = new InitialData();
				e.setStubURL(line.split(",")[0]);
				e.setStubEndpoint(line.split(",")[1]);
				list.add(e);
				//System.out.println("CsvParser() : " + line);	
			}
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		csvLogger.info("CsvParser.parseCsv() returning list of lines from csv");
		return linesList;
	}

	/**
	 * Method to read & parse csv file,
	 * 
	 * @return HashMap<Integer,String> Map of all entries in csv file
	 */
	public static HashMap<Integer, String> parseCsvWithIdUrl() {
		String CsvFileToParse = "C:\\Users\\Rohan.Pandhare\\Desktop\\Rohan\\CSVFiles\\csvMon_Jun_29_14_43_56_IST_2015.csv";
		csvLogger.info("CsvParser.parseCsvWithIdUrl() parsing file : " + CsvFileToParse);
		
		BufferedReader br = null;
		String line = "";
		String commaDelimeter = ",";

		HashMap<Integer, String> LinesHashMap = new HashMap<Integer, String>();

		try {
			br = new BufferedReader(new FileReader(CsvFileToParse));

			while ((line = br.readLine()) != null) {
				String[] IdUrl = line.split(commaDelimeter);

				LinesHashMap.put(Integer.parseInt(IdUrl[0].trim()),
						IdUrl[1].trim());
			}

			//System.out.println("Number of lines : " + LinesHashMap.size());
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		// System.out.println("Inside CsvParser.csvParse() method, Returning the list of urls from CSVFile=============>");
		return LinesHashMap;
	}
}
