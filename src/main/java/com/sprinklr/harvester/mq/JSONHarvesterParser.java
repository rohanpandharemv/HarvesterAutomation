package com.sprinklr.harvester.mq;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONHarvesterParser {

	/**
	 * Method to convert Json file to Csv file with values like
	 * authorId,authorName,mentionTime,overallRating,content
	 */
	public static void writeJsonToCsv(String json, FileWriter outCsvFile) {
		JSONParser parser = new JSONParser();

		String CommaDelimiter = ",";

		String outFileHeaders = "authorId,authorName,mentionTime,overallRating,content";

		String fileLocation = "C:\\Users\\Rohan.Pandhare\\Desktop\\Rohan\\CSVFiles";

		/*FileWriter outCsvFile = null;
		String OutFile = fileLocation + "\\csv"
				+ new Date().toString().replace(" ", "_").replace(":", "_")
				+ ".csv";*/

		try {
			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;

			//System.out.println("Writing to file : " + OutFile);
			//outCsvFile = new FileWriter(OutFile);

			outCsvFile.append(outFileHeaders.toString().trim());
			outCsvFile.append('\n');
			JSONArray arrayEntries = (JSONArray) jsonObject.get("entries");
			for (int i = 0; i < arrayEntries.size(); i++) {

				JSONObject entry = (JSONObject) arrayEntries.get(i);

				JSONObject author = (JSONObject) entry.get("author");

				System.out.println("1-1--1-2- authorId = "
						+ author.get("authorId"));
				outCsvFile.append(author.get("authorId").toString().trim());
				outCsvFile.append(CommaDelimiter);

				System.out.println("1-1--1-3- name = " + author.get("name"));
				outCsvFile.append(author.get("name").toString().trim());
				outCsvFile.append(CommaDelimiter);

				JSONObject document = (JSONObject) entry.get("document");

				System.out.println("mentionTime: "
						+ document.get("mentionTime"));
				outCsvFile
						.append(document.get("mentionTime").toString().trim());
				outCsvFile.append(CommaDelimiter);

				System.out.println("overallRating: "
						+ document.get("overallRating"));
				outCsvFile.append(document.get("overallRating").toString()
						.trim());
				outCsvFile.append(CommaDelimiter);

				System.out.println("Contents: " + document.get("content"));
				outCsvFile.append(document.get("content").toString().trim());
				outCsvFile.append('\n');
			}
			outCsvFile.flush();
			outCsvFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}