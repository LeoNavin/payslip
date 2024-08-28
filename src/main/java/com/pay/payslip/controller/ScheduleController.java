/**
 * 
 */
package com.pay.payslip.controller;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Leo Navin
 *
 */
public class ScheduleController {	
	
	//Example Running test the code file and this few lines only 
//	public static void main(String[] args) {
//        Timer timer = new Timer();
//        timer.schedule(new FileDeleteTask("D:/jar/folder/myfile.pdf"), 90000); // delete file after 60 seconds
//
//	}
	
	
	
	 static class FileDeleteTask extends TimerTask {

	        private String fileName;

	        public FileDeleteTask(String fileName) {
	            this.fileName = fileName;
	        }

	        public void run() {

	            File file = new File(fileName);
		     

	            if (file.exists()) {
	                file.delete();
	                System.out.println("File deleted");
	            } else {
	                System.out.println("File not found");
	            }
	        }
	    }

}
