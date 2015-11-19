package com.print.scheduler;

import java.util.Properties;

public class UserSetting {

	private String userName;
	private String invoicePrinterName;
	private String label_4_6_PrinterName;
	private String label_8_11_PrinterName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInvoicePrinterName() {
		return invoicePrinterName;
	}

	public void setInvoicePrinterName(String invoicePrinterName) {
		this.invoicePrinterName = invoicePrinterName;
	}

	public String getLabel_4_6_PrinterName() {
		return label_4_6_PrinterName;
	}

	public void setLabel_4_6_PrinterName(String label_4_6_PrinterName) {
		this.label_4_6_PrinterName = label_4_6_PrinterName;
	}

	public String getLabel_8_11_PrinterName() {
		return label_8_11_PrinterName;
	}

	public void setLabel_8_11_PrinterName(String label_8_11_PrinterName) {
		this.label_8_11_PrinterName = label_8_11_PrinterName;
	}

	public void loadUserSettings(String userName,Properties printerConfig){
		String tempUserName = printerConfig.getProperty(userName);
		if(tempUserName != null && !tempUserName.isEmpty()){
			this.userName = tempUserName;
			this.label_4_6_PrinterName =printerConfig.getProperty(tempUserName+"_LABEL_4.6_PRINTER_NAME");
			this.invoicePrinterName =printerConfig.getProperty(tempUserName+"_INVOICE_PRINTER_NAME");
			this.label_8_11_PrinterName =printerConfig.getProperty(tempUserName+"_LABEL_8.11_PRINTER_NAME");
		}
		
	}

}
