package com.sprinklr.harvester.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to read properties files values
 */
public class PropertyHandler {
	/**
	 * Method to fetch configuration properties from qa-db.properties file
	 * 
	 * @return java.util.Properties
	 */
	public static Properties getProperties() {
		Properties prop = new Properties();
		String prop_file_name = System.getProperty("user.dir") + "\\conf\\Harvester.properties";

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(prop_file_name);
			prop.load(inputStream);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prop;
	}

	/**
	 * Method to get property values from <source>.properties under
	 * src/main/resources
	 * 
	 * @return java.util.Properties
	 */
	public static Properties getSourceProperties() {
		Properties prop = new Properties();
		String source = PropertyHandler.getProperties().getProperty("source").toLowerCase();
		String prop_file_name = System.getProperty("user.dir") + "\\src\\main\\resources\\" + source + "\\" + source
		        + ".properties";
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(prop_file_name);
			prop.load(inputStream);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prop;
	}

}
