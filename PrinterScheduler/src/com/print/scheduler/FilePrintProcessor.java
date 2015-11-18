package com.print.scheduler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import pdftron.Common.PDFNetException;

public class FilePrintProcessor extends Thread{
	private static final Logger log = LogManager.getLogger(FilePrintProcessor.class);
	@Override
	public void run() {
		while (true){
			FilePrinter filePrinter = new FilePrinter();
				try {
					filePrinter.printProcessor();
				} catch (PDFNetException e1) {
					// TODO Auto-generated catch block
					log.info(e1);
				}  
		     
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.info(e);
			}
	}
	}
	
	public static void main(String[] args) {
		try{
		FilePrintProcessor filePrintProcessor = new FilePrintProcessor();
		filePrintProcessor.start();
		}catch(Exception e){
			log.info(e);
		}

	}

}
