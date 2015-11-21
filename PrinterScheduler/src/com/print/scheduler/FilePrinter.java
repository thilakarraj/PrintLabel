package com.print.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import pdftron.Common.PDFNetException;

public class FilePrinter extends Thread {
	private static final Logger log = LogManager.getLogger(FilePrinter.class);
	private static Properties propertyLocation = PropertyReader.getInstance()
			.getFileLocation();
	static String fileFormat =  PrinterConstants.PRINT_FILE_FORMAT;
	private static Properties printerConfig;
	String propertyFormat ="properties";
	String sourceFileLocation=  propertyLocation.getProperty(
			"PRINT_FILE_SOURCE_LOCATION");
	// seacrch PDF file in destigination path
	public List<String> getDirectoryFileList(String filePath) {
		List<String> fileList = new ArrayList<String>();
		File directory = new File(filePath);
		if (directory.isDirectory()) {
			log.info("Searching "+filePath+" directory ... ");
			File file = new File(filePath);
			if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.getName().toLowerCase().endsWith(fileFormat)) {
						fileList.add(temp.getAbsoluteFile().toString());
					}
				}
			}
		}
		return fileList;
	}

/*	// read values from printer property file
	public Properties getPrinterPorperty() {
		try {

			File parserfile = new File("conf\\" + "Printer.properties");
			if (!parserfile.exists()) {
				throw new Exception("Property file is not found");
			}
			InputStream inputStream = new FileInputStream(parserfile);
			printerDetails.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printerDetails;
	}*/

	public void moveFileToOutFolder(String fileName,String desLocation) {
		File file = new File(fileName);
		String outFileName = null;
		String outFolder = "";
		if (file != null) {
			outFolder = desLocation+"\\"+PrinterConstants.PROCESSED_DIRECTORY;
			String fileOldName = file.getName().replace("." + fileFormat, "");
			String date = new SimpleDateFormat("-MM-dd-yyyy HH-mm-ss")
					.format(new Date());
			outFileName = fileOldName+date+ "." + fileFormat;
			File outFile = new File(outFolder, outFileName);
			if (outFile.exists()) {
				outFile.delete();
				outFile = null;
				outFile = new File(outFolder, outFileName);
			}
			boolean success = file.renameTo(outFile);
			if (!success) {
				String msg = "Failed to moved to out Folder:" + outFolder
						+ " File:" + file.getName();
				log.info(msg);
			} else {
				log.info(" File:" + file.getName() + " successfully moved to "
						+ outFolder + " Folder");
			}
		}

	}
	
	// read printer configuration properties
	public String getPropertyFileLocation() {
		
		String filePath =sourceFileLocation.toString();
		File directory = new File(filePath);
		if (directory.isDirectory()) {
			log.info("Searching root directory ... ");
			File file = new File(filePath);
			if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.getName().toLowerCase().endsWith(propertyFormat)) {
						return temp.getAbsoluteFile().toString();
					}
				}
			}
		}
		return null;
	}

	
	public static void readDirectory(File node) {
		log.info(node.getAbsoluteFile());
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				readDirectory(new File(node, filename));
			}
		}

	}

	public void printProcessor() throws PDFNetException {
		FilePrinter printer = new FilePrinter();
		PDFPrinter pdfPrinter = new PDFPrinter();
		String printerName = null;
		String fileName = null;
		String sourceLoc = getPropertyFileLocation();
		if (sourceLoc != null && !sourceLoc.isEmpty()) {
			printerConfig = PropertyReader.getInstance().getPrinterConfig(
					sourceLoc);
		}
		File sourceFile = new File(sourceFileLocation);
		if (sourceFile.isDirectory()) {
			String[] subNote = sourceFile.list();
			if (subNote.length > 0) {
				for (String directoryName : subNote) {
					String subDirLoc = sourceFileLocation + "\\"
							+ directoryName;
					File subDir = new File(subDirLoc);
					if (subDir.isDirectory()) {
						UserSetting setting = new UserSetting();
						setting.loadUserSettings(directoryName, printerConfig);
						String[] subNote1 = subDir.list();
						for (String directoryName1 : subNote1) {
							
							if (!directoryName1.equalsIgnoreCase(propertyLocation.getProperty("PROCESSED_DIRECTORY"))) {
								if(PrinterConstants.INVOICE_DIRECTORY.equalsIgnoreCase(directoryName1)){
									printerName = setting.getInvoicePrinterName();
								}
								if(PrinterConstants.LABEL_4_6_DIRECTORY.equalsIgnoreCase(directoryName1)){
									printerName = setting.getLabel_4_6_PrinterName();
								}
								if(PrinterConstants.LABEL_8_11_DIRECTORY.equalsIgnoreCase(directoryName1)){
									printerName = setting.getLabel_8_11_PrinterName();
								}
								String subDirLoc1 = subDirLoc + "\\"+ directoryName1;
								List<String> filePaths = getDirectoryFileList(subDirLoc1);
								if (filePaths != null && filePaths.size() >= 0) {
									for (String temp : filePaths) {
										pdfPrinter.doPrint(temp, printerName);
										printer.moveFileToOutFolder(temp,subDirLoc);
									}
								}
							}
						}
					}
				}
			}
		}
		/*pdfPrinter.doPrint(fileName, printerName);
		printer.moveFileToOutFolder(fileName);*/
		// pdfPrinter = null;
	}
	
}
