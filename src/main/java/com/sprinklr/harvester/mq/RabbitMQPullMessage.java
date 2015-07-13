package com.sprinklr.harvester.mq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.sprinklr.harvester.model.ReviewData;
import com.sprinklr.harvester.util.PropertyHandler;

public class RabbitMQPullMessage {

	public final static String QUEUE_NAME = PropertyHandler.getProperties()
			.getProperty("pull_queue");

	/**
	 * Method to read data in Json Format & extract values out of it and returns
	 * a HashMap
	 * 
	 * @param message
	 *            Json File data in String format
	 * @return HashMap<String,ArrayList<ReviewData>>
	 */
	public static HashMap<String, ArrayList<ReviewData>> writeJsonToHashmap(
			String message) {
		HashMap<String, ArrayList<ReviewData>> reviewContent = new HashMap<String, ArrayList<ReviewData>>();

		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(message);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray jsonEntries = (JSONArray) jsonObject.get("entries");
			JSONObject record = (JSONObject) jsonObject.get("record");

			for (int i = 0; i < jsonEntries.size(); i++) {
				ReviewData rdObject = new ReviewData();

				rdObject.setHarvesterID(record.get("harvesterId").toString());

				JSONObject entry = (JSONObject) jsonEntries.get(i);

				JSONObject author = (JSONObject) entry.get("author");

				rdObject.setAuthorId(author.get("authorId").toString().trim());

				JSONObject document = (JSONObject) entry.get("document");

				rdObject.setMentionedDate(document.get("mentionTime")
						.toString().trim());

				rdObject.setRatings(document.get("overallRating").toString()
						.trim());

				rdObject.setComment(document.get("content").toString().trim());

				rdObject.setHarvesterID(record.get("harvesterId").toString());

				System.out.println(rdObject.toString());

				Set<String> reviewContentKeyset = reviewContent.keySet();
				if (reviewContentKeyset.contains(rdObject.getAuthorId())) {
					ArrayList<ReviewData> reviewData = new ArrayList<ReviewData>();
					reviewData = reviewContent.get(rdObject.getAuthorId());
					reviewData.add(rdObject);
					reviewContent.put(rdObject.getAuthorId(), reviewData);
				} else {
					ArrayList<ReviewData> reviewData = new ArrayList<ReviewData>();
					reviewData.add(rdObject);
					reviewContent.put(rdObject.getAuthorId(), reviewData);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return reviewContent;
	}

	public static String getHarvesterID(String message) {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(message);
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject record = (JSONObject) jsonObject.get("record");
			String harvesterid = record.get("harvesterId").toString();
			return harvesterid;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "0";
	}

	public static HashMap<String, HashMap<String, ArrayList<ReviewData>>> pull() {

		HashMap<String, HashMap<String, ArrayList<ReviewData>>> actualReviewDataPerStub = new HashMap<String, HashMap<String, ArrayList<ReviewData>>>();

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(PropertyHandler.getProperties().getProperty(
					"mqhost"));
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'");
				String stubID = getHarvesterID(message);
				HashMap<String, ArrayList<ReviewData>> actualDataByAuthorID = writeJsonToHashmap(message);
				actualReviewDataPerStub.put(stubID, actualDataByAuthorID);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return actualReviewDataPerStub;
	}

	/**
	 * Method to read data from file in json format, convert data to Simple
	 * String & return it
	 * 
	 * @return StringBuffer
	 * @throws Exception
	 */
	public static StringBuffer readJsonFromFile() throws Exception {
		File readFile = new File(
				"C:\\Users\\Rohan.Pandhare\\Desktop\\JsonText.txt");
		BufferedReader br = new BufferedReader(new FileReader(readFile));
		StringBuffer message = new StringBuffer();
		String line;

		while ((line = br.readLine()) != null) {
			message.append(line);
		}
		return message;
	}

	public static void main(String[] args) {

		try {
			StringBuffer message = readJsonFromFile();
			HashMap<String, ArrayList<ReviewData>> hashmap = writeJsonToHashmap(message
					.toString());

			if (hashmap != null) {
				System.out.println("Size of hashmap : " + hashmap.size());
			}

			System.out
					.println("*****************************************************************************************");

			// Logic to traverse the hashmap returned by writeJsonToHashMap()

			Collection<ArrayList<ReviewData>> ValueList = hashmap.values();
			ArrayList<ReviewData> reviewDataList;
			Iterator<ArrayList<ReviewData>> vitr = ValueList.iterator();

			while (vitr.hasNext()) {
				reviewDataList = (ArrayList<ReviewData>) vitr.next();

				Iterator<ReviewData> rdlitr = reviewDataList.iterator();

				while (rdlitr.hasNext()) {
					ReviewData rd = (ReviewData) rdlitr.next();
					System.out.println(rd.toString());
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}