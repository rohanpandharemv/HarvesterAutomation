package com.sprinklr.harvester.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sprinklr.harvester.model.InitialData;

/**
 * Class to parse csv file
 * 
 * @author Rohan.Pandhare
 *
 */
public class CsvParser {

	private static ArrayList<InitialData> list = new ArrayList<InitialData>();

	public static ArrayList<InitialData> getStubCSVData() {
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
				System.out.println("CsvParser() : " + line);
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
		return linesList;
	}

	/**
	 * Method to read & parse csv file,
	 * 
	 * @return HashMap<Integer,String> Map of all entries in csv file
	 */
	public static HashMap<Integer, String> parseCsvWithIdUrl() {
		String CsvFileToParse = "C:\\Users\\Rohan.Pandhare\\Desktop\\Rohan\\CSVFiles\\csvMon_Jun_29_14_43_56_IST_2015.csv";

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

			System.out.println("Number of lines : " + LinesHashMap.size());
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
