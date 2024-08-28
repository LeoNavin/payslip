/**
 * 
 */
package com.pay.payslip.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Leo Navin
 *
 */
public final class ConstantUtil {
	public static final String CHAR_LIST = "1234567890";
	public static final String CHAR_AND_NUMBER_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMOPQRSTUVWXYZ*#$%&!1234567890";


	public final static List<String> USERROLES = new ArrayList<>(Arrays.asList("SUPER", "AIDBES", "BOARD ADMIN",
			"CONVENER", "CONSULTANT", "SECTOR ADMIN", "ASSESSOR", "INSTITUTION ADMIN", "ASSESSOR TEAM HEAD"));
	
	
//	public static String CONTEXT_PATH = "D:/Leo Navin/Springboot/springproject/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/vericea/static/report/";
//	public static String REPORT_PATH = "http://192.168.1.141:8082/vericea/static/report/";
//	public static String APP_URL = "http://192.168.1.141:8082/vericea";
//	public static Integer S_ADDRESS = -959020536; // 1982350832; // 199805549l  -959020536
//	public static String C_HOME = "D:/Leo Navin/Vericea/vericea source/apache-tomcat-8.0.38";
	
//	public static String CONTEXT_PATH = "C:/Users/BoscoSoft/Downloads/";
//	public static String REPORT_PATH = "C:/Users/BoscoSoft/Downloads/";D:\Leo Navin\Springboot\springproject\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\payslip\static\report
	public static String CONTEXT_PATH = "D:\\Leo Navin\\Springboot\\springproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\payslip\\static\\report\\";
//	public static String REPORT_PATH = "http://192.168.1.141:8088/Leo Navin/Springboot/springproject/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/payslip/static/report/";
//	public static String REPORT_PATH = "http://192.168.1.141:8088/payslip/static/report/";
	public static String REPORT_PATH = "http://192.168.1.141:8088/";

//	public static String REPORT_PATH = "D:/Leo Navin/Vericea/payslip/backend/payslip/src/main/resourcesstatic/files/";
//	public static String REPORT_PATH = "http://192.168.1.141//payslip/src/main/webapp/static/payslip";

	
	
//	public static String REPORT_PATH = "C:/Users/BoscoSoft/Downloads/";
//	public static String APP_URL = "http://192.168.1.141:8082/vericea";
//	public static Integer S_ADDRESS = -959020536; // 1982350832; // 199805549l  -959020536
//	public static String C_HOME = "D:/Leo Navin/Vericea/vericea source/apache-tomcat-8.0.38";
	
	
	
	public static final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
			"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
			"Nineteen" };
	
	
	
	public static final String[] tens = { "", // 0
			"", // 1
			"Twenty", // 2
			"Thirty", // 3
			"Forty", // 4
			"Fifty", // 5
			"Sixty", // 6
			"Seventy", // 7
			"Eighty", // 8
			"Ninety" // 9
	};
	
	
	public final static String PAYSLIP_CONTEXT_PATH = "/var/www/html/payslipReport/";
	public final static String PAYSLIP_REPORT_PATH = "http://172.104.97.178/payslipReport/";
	
	public final static String PAYSLIP_User_Profile_CONTEXT_PATH = "/var/www/html/payslipProfile/";
	public final static String PAYSLIP_User_Profile_REPORT_PATH = "http://172.104.97.178/payslipProfile/";

	
//	public final static String PAYMENT_INST_CONFIRM_PAGE_URL = "http://192.168.43.198:4200/#/institutionpayment/";

	
//	public final static String PAYSLIP_CONTEXT_PATH = "/var/www/html/payslipReport/";
//	public final static String PAYSLIP_REPORT_PATH = "http://localhost/payslipReport/";
	
}
 