package com.sprinklr.harvester.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class to parse csv file
 * 
 * @author Rohan.Pandhare
 *
 */
public class CsvParser {

	/**
	 * Method to read & parse csv file,
	 * 
	 * @return List<String> List of all entries in csv file
	 */
	public static List<String> parseCsv() {
		String source = PropertyHandler.getProperties().getProperty("source");
		String csvFileToParse = System.getProperty("user.dir")
				+ "\\src\\main\\resources\\" + source.toLowerCase() + ".csv";
		// String CsvFileToParse =
		// "C:\\Users\\Rohan.Pandhare\\Desktop\\ctrip.urls.prod-db2.060315.csv";

		BufferedReader br = null;
		String line = "";
		List<String> LinesList = new ArrayList<String>();

		try {
			br = new BufferedReader(new FileReader(csvFileToParse));

			while ((line = br.readLine()) != null) {
				LinesList.add(line);
				System.out.println("CsvParser() : " + line);
			}

			// System.out.println("Number of lines : " + LinesList.size());
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
		return LinesList;
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
		String newLineSeparator = "\n";

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

	public static void main(String[] args) {
		HashMap<Integer, String> fileData = parseCsvWithIdUrl();
		Set<Integer> keySet = fileData.keySet();
		Collection<String> url = fileData.values();
		Iterator<Integer> kitr = keySet.iterator();
		Iterator<String> uitr = url.iterator();

		while (kitr.hasNext()) {
			System.out.println("ID : " + kitr.next());
			System.out.println("URL : " + uitr.next());
			System.out
					.println("----------------------------------------------------");
		}
	}
}
