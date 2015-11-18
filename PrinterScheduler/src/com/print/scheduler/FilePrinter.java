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
public class FilePrinter extends Thread{
	private static final Logger log = LogManager.getLogger(FilePrinter.class);
	
	private static Properties printerDetails = new Properties();
	static String fileFormat = printerDetails.getProperty("PRINT_FILE_FORMAT") ;
	
	
	// seacrch PDF file in destigination path 
	public List<String> getDirectoryFileList() {
		printerDetails = getPrinterPorperty();
		fileFormat = printerDetails.getProperty("PRINT_FILE_FORMAT") ;
		List<String> fileList = new ArrayList<String>();
		String filePath = printerDetails.getProperty("PRINT_FILE_SOURCE_LOCATION")
				.toString();
		File directory = new File(filePath);
		if (directory.isDirectory()) {
			log.info("Searching directory ... ");
			File file = new File(filePath);
			if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.getName().toLowerCase()
									.endsWith(fileFormat)) {
						fileList.add(temp.getAbsoluteFile().toString());
					}
				}
			}
		}
		return fileList;
	}
	
	// read values from printer property file
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
	}

	
	public  void moveFileToOutFolder(String fileName) {
		File file = new File(fileName);
		String outFileName = null;
		String outFolder = "";
		if (file != null) {
			 outFolder = printerDetails
					.getProperty("PRINT_FILE_DESIGNATION_LOCATION");
			String fileOldName = file.getName().replace("."+fileFormat, "");
			String date = new SimpleDateFormat("-MM-dd-yyyy HH-mm-ss")
					.format(new Date());
			outFileName = fileOldName+"."+ fileFormat;
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
	
	public void printProcessor() throws PDFNetException {
		List<String> fileList = new ArrayList<String>();
		FilePrinter printer = new FilePrinter();
		PDFPrinter pdfPrinter = new PDFPrinter();
		fileList = printer.getDirectoryFileList();
		if (fileList == null && (fileList.size() == 0)) {
			log.info("No import file found!");
		} else {
			log.info(fileList.size() + " " + fileFormat + " files found!");
			for (String fileName : fileList) {
				pdfPrinter.doPrint(fileName);
				printer.moveFileToOutFolder(fileName);
				//pdfPrinter = null;
		}
	}
	}

}
