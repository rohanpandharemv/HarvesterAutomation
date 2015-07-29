package com.sprinklr.harvester.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sprinklr.harvester.model.InitialData;

/**
 * Class to parse CSV file
 */
public class CsvParser {

	public final static Logger LOGGER = Logger.getLogger(CsvParser.class);

	private static ArrayList<InitialData> list = new ArrayList<InitialData>();

	public static ArrayList<InitialData> getStubCSVData() {
		LOGGER.info("CsvParser.getStubCSVData() returning list InitialData objects");
		return list;
	}

	/**
	 * Method to read & parse CSV file, based on the given <source> in
	 * Harvester.properties
	 * 
	 * @return List<String> List of all entries in CSV file
	 */
	public static List<String> parseCsv() {

		list = new ArrayList<InitialData>();
		String source = PropertyHandler.getProperties().getProperty("source").toLowerCase();
		String csvFileToParse = System.getProperty("user.dir") + "\\src\\main\\resources\\" + source + "\\" + source
		        + ".csv";
		LOGGER.info("CsvParser.parseCsv() parsing csv file : " + csvFileToParse);
		BufferedReader bufferedReader = null;
		String line = "";
		List<String> linesList = new ArrayList<String>();

		try {
			bufferedReader = new BufferedReader(new FileReader(csvFileToParse));

			while ((line = bufferedReader.readLine()) != null) {
				linesList.add(line.split(",")[0]);
				InitialData e = new InitialData();
				e.setStubURL(line.split(",")[0]);
				e.setStubEndpoint(line.split(",")[1]);
				list.add(e);
			}
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		LOGGER.info("CsvParser.parseCsv() returning list of lines from csv");
		return linesList;
	}
}
