package com.print.scheduler;

/**
* The PropertyReader program read printer Property file
* @author  Thilakar Raj
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class PropertyReader {
	private static PropertyReader propertyObj;
	private static Properties sourceDetails = new Properties();
	private static Properties printerConfig = new Properties();
    
	private PropertyReader() {
		// TODO Auto-generated constructor stub
	}
	// The getInstance() purpose is to control object creation, limiting the number of obejcts to one only.
	public static PropertyReader getInstance() {
		if (propertyObj == null) {
			propertyObj = new PropertyReader();
		}
		return propertyObj;
	}

	public Properties getFileLocation() {
		try {

			File parserfile = new File("conf\\" + "Printer.properties");
			if (!parserfile.exists()) {
				throw new Exception("Property file is not found");
			}
			InputStream inputStream = new FileInputStream(parserfile);
			sourceDetails.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceDetails;
	}
	
	public Properties getPrinterConfig(String sourcePath) {
		try {

			File parserfile = new File(sourcePath);
			if (!parserfile.exists()) {
				throw new Exception("Property file is not found");
			}
			InputStream inputStream = new FileInputStream(parserfile);
			sourceDetails.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceDetails;
	}
}
