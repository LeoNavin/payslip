/**
 * 
 */
package com.pay.payslip.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.dto.EmployeesBulkUploadPayslip;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;
import com.pay.payslip.model.User;
import com.pay.payslip.servie.MonthAndYearService;
import com.pay.payslip.servie.PaySlipService;
import com.pay.payslip.servie.RoleService;
import com.pay.payslip.servie.UserService;
import com.pay.payslip.util.FilterUtil;
import com.pay.payslip.util.ResponseModel;

/**
 * @author Leo Navin
 *
 */

@RestController
@RequestMapping(value = "/payslipBulkUpload")
public class PaySlipBulkUploadController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private PaySlipService paySlipService;

	@Autowired
	private MonthAndYearService monthAndYearService;

	private ResponseModel response = new ResponseModel();

	private MappingJacksonValue mappingJacksonValue;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

	@GetMapping(value = "/exportEmployeePaySlipTemplate")
	public void exportProductuploadTemplate(@RequestHeader("Authorization") String token, HttpServletResponse response,
			@RequestParam("id") Long monthId) throws IOException {
//		String productLabel = principalUtil.getPoductLabel();
//		String productTypeLabel = principalUtil.getProductTypeLabel();

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent() && logInUser.get().getRole().getRole().equals("ADMIN")) {

			MonthAndYear payslip = monthAndYearService.getMonthById(monthId);

			Date date = new Date();

			response.setContentType("application/vnd.ms-excel");
			String fileName = "Salary_Slip" + "_BulkUpload_Template_For_" + payslip.getMonth().getMonth() + "_"
					+ payslip.getYear() + date.getTime() + ".xls";
			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=\"" + fileName.replaceAll("\\s+", "_") + "\"";

			String pragma = "Pragma";
			String pragmaValue = fileName.replaceAll("\\s+", "_");

			response.setHeader(headerKey, headerValue);

			response.setHeader(pragma, pragmaValue);

//			String fileName = "Employee PaySlip" + " BulkUpload Template" + date.getTime() + ".xls";
//			String contextPath = ConstantUtil.CONTEXT_PATH;
//			FileOutputStream fileOutputStream = null;
//			File file = null;
//			try {
//				file = new File(contextPath + fileName);
//				if (!file.exists()) {
//					file.createNewFile();
//				}
			OutputStream out = response.getOutputStream();
//			} catch (FileNotFoundException ex) {
//				ex.printStackTrace();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet hssfSheet = hssfWorkbook.createSheet("Salary" + " Details ");
			HSSFSheet hssfSheet2 = hssfWorkbook.createSheet("Employee" + " Details ");

			HSSFRow headerRow0 = hssfSheet.createRow(0);
			hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 43));

			HSSFRow headerRow = hssfSheet.createRow(1);

			HSSFRow headerRowSheet2 = hssfSheet2.createRow(0);

			org.apache.poi.ss.usermodel.Font boldFont = hssfWorkbook.createFont();
			boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle tableHeaderStyle = hssfWorkbook.createCellStyle();
			tableHeaderStyle.setBorderBottom(CellStyle.BORDER_THIN);
			tableHeaderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			tableHeaderStyle.setBorderLeft(CellStyle.BORDER_THIN);
			tableHeaderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			tableHeaderStyle.setBorderRight(CellStyle.BORDER_THIN);
			tableHeaderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			tableHeaderStyle.setBorderTop(CellStyle.BORDER_THIN);
			tableHeaderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			tableHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			tableHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
			tableHeaderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			tableHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			tableHeaderStyle.setFont(boldFont);

			CellStyle tableHeaderStyle1 = hssfWorkbook.createCellStyle();
			Font font = hssfWorkbook.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			tableHeaderStyle1.setFont(font);

			HSSFCell bosco = headerRow0.createCell(0);
			bosco.setCellValue(/* "Employee" + */ " Bosco Soft Technologies Pvt Ltd - Salary for the Month of "
					+ payslip.getMonth().getMonth() + " - " + payslip.getYear());
			bosco.setCellStyle(tableHeaderStyle);
			bosco.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empSerialNo = headerRow.createCell(0);
			empSerialNo.setCellValue(/* "Employee" + */ "S.No");
			empSerialNo.setCellStyle(tableHeaderStyle);
			empSerialNo.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empId = headerRow.createCell(1);
			empId.setCellValue(/* "Employee" + */ "Associate Id");
			empId.setCellStyle(tableHeaderStyle);
			empId.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empName = headerRow.createCell(2);
			empName.setCellValue(/* "Employee" + */ "Associate Name");
			empName.setCellStyle(tableHeaderStyle);
			empName.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empDesignation = headerRow.createCell(3);
			empDesignation.setCellValue(/* "Employee" + */ "Designation");
			empDesignation.setCellStyle(tableHeaderStyle);
			empDesignation.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empGender = headerRow.createCell(4);
			empGender.setCellValue(/* "Employee" + */ "Gender");
			empGender.setCellStyle(tableHeaderStyle);
			empGender.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empUnit = headerRow.createCell(5);
			empUnit.setCellValue(/* "Employee" + */ "Unit");
			empUnit.setCellStyle(tableHeaderStyle);
			empUnit.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empDOJ = headerRow.createCell(6);
			empDOJ.setCellValue(/* "Employee" + */ "D.O.J");
			empDOJ.setCellStyle(tableHeaderStyle);
			empDOJ.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell empGross = headerRow.createCell(7);
			empGross.setCellValue(/* "Employee" + */ "Gross");
			empGross.setCellStyle(tableHeaderStyle);
			empGross.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell basic = headerRow.createCell(8);
			basic.setCellValue(/* "Employee" + */ "Basic");
			basic.setCellStyle(tableHeaderStyle);
//			basic.setCellFormula("=C3*60%");
			basic.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell hra = headerRow.createCell(9);
			hra.setCellValue(/* "Employee" + */ "HRA");
//			hra.setCellFormula("=D3*40%");
			hra.setCellStyle(tableHeaderStyle);

			hra.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell lta = headerRow.createCell(10);
			lta.setCellValue(/* "Employee" + */ "LTA");
			lta.setCellStyle(tableHeaderStyle);
			lta.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell medicalAll = headerRow.createCell(11);
			medicalAll.setCellValue(/* "Employee" + */"Medical All");
			medicalAll.setCellStyle(tableHeaderStyle);
			medicalAll.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell conveyance = headerRow.createCell(12);
			conveyance.setCellValue(/* "Employee" + */ "Conveyance");
			conveyance.setCellStyle(tableHeaderStyle);
			conveyance.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell splAllow = headerRow.createCell(13);
			splAllow.setCellValue(/* "Employee" + */ "Spl allow");
			splAllow.setCellStyle(tableHeaderStyle);
			splAllow.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell grossEarning = headerRow.createCell(14);
			grossEarning.setCellValue(/* "Employee" + */"Gross Earnings");
			grossEarning.setCellStyle(tableHeaderStyle);
			grossEarning.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell daysMonth = headerRow.createCell(15);
			daysMonth.setCellValue(/* "Employee" + */ "#days/  month");
			daysMonth.setCellStyle(tableHeaderStyle);
			daysMonth.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell daysWorked = headerRow.createCell(16);
			daysWorked.setCellValue(/* "Employee" + */ "#days/  worked");
			daysWorked.setCellStyle(tableHeaderStyle);
			daysWorked.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell daysAbsent = headerRow.createCell(17);
			daysAbsent.setCellValue(/* "Employee" + */ "# of days Absent");
			daysAbsent.setCellStyle(tableHeaderStyle);
			daysAbsent.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell basic_1 = headerRow.createCell(18);
			basic_1.setCellValue(/* "Employee" + */ "Basic");
			basic_1.setCellStyle(tableHeaderStyle);
//			basic.setCellFormula("=C3*60%");
			basic_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell hra_1 = headerRow.createCell(19);
			hra_1.setCellValue(/* "Employee" + */ "HRA");
			hra_1.setCellStyle(tableHeaderStyle);
			hra_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell lta_1 = headerRow.createCell(20);
			lta_1.setCellValue(/* "Employee" + */ "LTA");
			lta_1.setCellStyle(tableHeaderStyle);
			lta_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell medicalAll_1 = headerRow.createCell(21);
			medicalAll_1.setCellValue(/* "Employee" + */"Medical All");
			medicalAll_1.setCellStyle(tableHeaderStyle);
			medicalAll_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell conveyance_1 = headerRow.createCell(22);
			conveyance_1.setCellValue(/* "Employee" + */ "Conveyance");
			conveyance_1.setCellStyle(tableHeaderStyle);
			conveyance_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell splAllow_1 = headerRow.createCell(23);
			splAllow_1.setCellValue(/* "Employee" + */ "Spl allow");
			splAllow_1.setCellStyle(tableHeaderStyle);
			splAllow_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell epfGross = headerRow.createCell(24);
			epfGross.setCellValue(/* "Employee" + */ "epf Gross");
			epfGross.setCellStyle(tableHeaderStyle);
			epfGross.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell grossEarning_1 = headerRow.createCell(25);
			grossEarning_1.setCellValue(/* "Employee" + */"Gross Earnings");
			grossEarning_1.setCellStyle(tableHeaderStyle);
			grossEarning_1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell pf = headerRow.createCell(26);
			pf.setCellValue(/* "Employee" + */ "PF");
			pf.setCellStyle(tableHeaderStyle);
			pf.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell ctc = headerRow.createCell(27);
			ctc.setCellValue(/* "Employee" + */ "CTC");
			ctc.setCellStyle(tableHeaderStyle);
			ctc.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell pfEmployerContr = headerRow.createCell(28);
			pfEmployerContr.setCellValue(/* "Employee" + */ "PF Employer Contr 12%");
			pfEmployerContr.setCellStyle(tableHeaderStyle);
			pfEmployerContr.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell tds = headerRow.createCell(29);
			tds.setCellValue(/* "Employee" + */ "TDS");
			tds.setCellStyle(tableHeaderStyle);
			tds.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell mess = headerRow.createCell(30);
			mess.setCellValue(/* "Employee" + */ "Mess");
			mess.setCellStyle(tableHeaderStyle);
			mess.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell hostel = headerRow.createCell(31);
			hostel.setCellValue(/* "Employee" + */ "Hostel");
			hostel.setCellStyle(tableHeaderStyle);
			hostel.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell sbf = headerRow.createCell(32);
			sbf.setCellValue(/* "Employee" + */ "SBF");
			sbf.setCellStyle(tableHeaderStyle);
			sbf.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell advanceLoan = headerRow.createCell(33);
			advanceLoan.setCellValue(/* "Employee" + */ "Sal advance");
			advanceLoan.setCellStyle(tableHeaderStyle);
			advanceLoan.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell cityAllowance = headerRow.createCell(34);
			cityAllowance.setCellValue(/* "Employee" + */ "City Allowance");
			cityAllowance.setCellStyle(tableHeaderStyle);
			cityAllowance.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell insurance = headerRow.createCell(35);
			insurance.setCellValue(/* "Employee" + */ "Insurance");
			insurance.setCellStyle(tableHeaderStyle);
			insurance.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell totalDeduction = headerRow.createCell(36);
			totalDeduction.setCellValue(/* "Employee" + */ "Total deduction");
			totalDeduction.setCellStyle(tableHeaderStyle);
			totalDeduction.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell netPay = headerRow.createCell(37);
			netPay.setCellValue(/* "Employee" + */ "Net pay 19");
			netPay.setCellStyle(tableHeaderStyle);
			netPay.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell bankMode = headerRow.createCell(38);
			bankMode.setCellValue(/* "Employee" + */ "Bank Mode");
			bankMode.setCellStyle(tableHeaderStyle);
			bankMode.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell bankAccount = headerRow.createCell(39);
			bankAccount.setCellValue(/* "Employee" + */ "Bank Account");
			bankAccount.setCellStyle(tableHeaderStyle);
			bankAccount.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell part1 = headerRow.createCell(40);
			part1.setCellValue(/* "Employee" + */ "Part I");
			part1.setCellStyle(tableHeaderStyle);
			part1.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell part2 = headerRow.createCell(41);
			part2.setCellValue(/* "Employee" + */ "Part II");
			part2.setCellStyle(tableHeaderStyle);
			part2.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell amount = headerRow.createCell(42);
			amount.setCellValue(/* "Employee" + */ "Amount");
			amount.setCellStyle(tableHeaderStyle);
			amount.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell lop = headerRow.createCell(43);
			lop.setCellValue(/* "Employee" + */ "LOP");
			lop.setCellStyle(tableHeaderStyle);
			lop.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell employId = headerRowSheet2.createCell(0);
			employId.setCellValue(/* "Employee" + */ "Associate Id" + "*");
			employId.setCellStyle(tableHeaderStyle);
			employId.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell employName = headerRowSheet2.createCell(1);
			employName.setCellValue(/* "Employee" + */ "Associate Name" + "*");
			employName.setCellStyle(tableHeaderStyle);
			employName.setCellType(Cell.CELL_TYPE_STRING);

			List<User> userList = userService.findByAlluser(logInUser.get().getId());
			int rowIndex = 1;

			for (User user : userList) {
				HSSFRow headerRow1Sheet2 = hssfSheet2.createRow(rowIndex);

				if (user != null) {

					HSSFCell employeeId = headerRow1Sheet2.createCell(0);
					employeeId.setCellValue(user.getAssociateId().toString());
//					employeeId.setCellType(Cell.CELL_TYPE_STRING);
//					employeeId.setCellType(Cell.CELL_TYPE_NUMERIC);
//					System.out.println(((Object) user.getAssociateId().getClass().getSimpleName()));

					HSSFCell employeeName = headerRow1Sheet2.createCell(1);
					employeeName.setCellValue(user.getFirstname());
//					employeeName.setCellType(Cell.CELL_TYPE_STRING);
				}
				rowIndex++;
			}

			hssfSheet2.autoSizeColumn(0);
			hssfSheet2.autoSizeColumn(1);

			hssfSheet.autoSizeColumn(0);
			hssfSheet.autoSizeColumn(1);
			hssfSheet.autoSizeColumn(2);
			hssfSheet.autoSizeColumn(3);
			hssfSheet.autoSizeColumn(4);
			hssfSheet.autoSizeColumn(5);
			hssfSheet.autoSizeColumn(6);
			hssfSheet.autoSizeColumn(7);
			hssfSheet.autoSizeColumn(8);
			hssfSheet.autoSizeColumn(9);
			hssfSheet.autoSizeColumn(10);
			hssfSheet.autoSizeColumn(11);
			hssfSheet.autoSizeColumn(12);
			hssfSheet.autoSizeColumn(13);
			hssfSheet.autoSizeColumn(14);
			hssfSheet.autoSizeColumn(15);
			hssfSheet.autoSizeColumn(16);
			hssfSheet.autoSizeColumn(17);
			hssfSheet.autoSizeColumn(18);
			hssfSheet.autoSizeColumn(19);
			hssfSheet.autoSizeColumn(20);
			hssfSheet.autoSizeColumn(21);
			hssfSheet.autoSizeColumn(22);
			hssfSheet.autoSizeColumn(23);
			hssfSheet.autoSizeColumn(24);
			hssfSheet.autoSizeColumn(25);
			hssfSheet.autoSizeColumn(26);
			hssfSheet.autoSizeColumn(27);
			hssfSheet.autoSizeColumn(28);
			hssfSheet.autoSizeColumn(29);
			hssfSheet.autoSizeColumn(30);
			hssfSheet.autoSizeColumn(31);
			hssfSheet.autoSizeColumn(32);
			hssfSheet.autoSizeColumn(33);
			hssfSheet.autoSizeColumn(34);
			hssfSheet.autoSizeColumn(35);
			hssfSheet.autoSizeColumn(36);
			hssfSheet.autoSizeColumn(37);
			hssfSheet.autoSizeColumn(38);
			hssfSheet.autoSizeColumn(39);
			hssfSheet.autoSizeColumn(40);
			hssfSheet.autoSizeColumn(41);
			hssfSheet.autoSizeColumn(42);

			try {
				if (hssfWorkbook != null) {
					hssfWorkbook.write(out);
				}
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//			return responseCreation(true, "Success", "Get All User data", ConstantUtil.REPORT_PATH + fileName, null);

		}
//		return null;
	}

	@PostMapping(value = "/uploadBulkEmployeePaySlip")
	public ResponseEntity<Object> uploadBulkEmployeePayslip(MultipartHttpServletRequest re,
			@RequestHeader("Authorization") String token, @RequestParam("id") Long id) throws IOException {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent() && logInUser.get().getRole().getRole().equals("ADMIN")) {

			EmployeesBulkUploadPayslip employeeBulkUploadPayslip = new EmployeesBulkUploadPayslip();
			Set<String> UniqueEmployeeAssociateIdList = new HashSet<String>();
//			Set<User> UniqueEmployeeAssociaList = new HashSet<User>();
//			Set<String> UniqueEmployeeUANList = new HashSet<String>();
//			Set<String> UniqueEmployeeBankAccountNumberList = new HashSet<String>();

			MultipartFile file = re.getFile("file");
			Workbook workbook = null;
			FileInputStream fileInputStream = null;
			ByteArrayInputStream byteArrayInputStream = null;

			List<EmployeesBulkUploadPayslip> listEmployeeBulkUploadsPayslip = new ArrayList<EmployeesBulkUploadPayslip>();
			List<String> finalErrorMessageList = new ArrayList<>();

			Row row = null;

			try {
				fileInputStream = (FileInputStream) file.getInputStream();
			} catch (Exception ex) {
				byteArrayInputStream = (ByteArrayInputStream) file.getInputStream();
			}
			if (fileInputStream != null) {
				try {
					workbook = WorkbookFactory.create(fileInputStream);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (byteArrayInputStream != null) {
				try {
					workbook = WorkbookFactory.create(byteArrayInputStream);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				int lastRowNum = sheet.getLastRowNum();
				boolean templateValidationFlag = validatePaySlipDataUpload(sheet.getRow(1));

				if (templateValidationFlag) {

					if (lastRowNum > 1) {
						for (int j = 2; j <= lastRowNum; j++) {
							row = sheet.getRow(j);

							employeeBulkUploadPayslip = constructEmployeeFromExcel(row, logInUser.get().getUsername(),
									id);

							if (employeeBulkUploadPayslip != null) {
//								if (!employeeBulkUploadPayslip.getError().equals(false)
//										&& employeeBulkUploadPayslip.getErrorMessage() != null) {
								if (employeeBulkUploadPayslip.getErrorMessage().size()>0) {
									
									List<String> message = employeeBulkUploadPayslip.getErrorMessage();
									finalErrorMessageList.addAll(message);

								} else if (!employeeBulkUploadPayslip.getError().equals(false)
										&& employeeBulkUploadPayslip.getErrorMessage() != null) {
									
									List<String> message = employeeBulkUploadPayslip.getErrorMessage();
									finalErrorMessageList.addAll(message);
									
								} else if (employeeBulkUploadPayslip.getPaySlip() != null
										&& employeeBulkUploadPayslip.getPaySlip().getUser().getAssociateId() != null) {

									String employeeAssociateIdAndMonthAndYearId = employeeBulkUploadPayslip.getPaySlip()
											.getUser().getAssociateId() + ","
											+ employeeBulkUploadPayslip.getPaySlip().getMonthAndYear().getId();

									if (!UniqueEmployeeAssociateIdList.add(employeeAssociateIdAndMonthAndYearId)) {
										int duplicateProductCodeRow = j + 1;
										String message = "Duplicate "
												+ employeeBulkUploadPayslip.getPaySlip().getUser().getAssociateId()
												+ " And"
												+ employeeBulkUploadPayslip.getPaySlip().getMonthAndYear().getId()
												+ " Code in row : ";

										finalErrorMessageList.add(message + " " + duplicateProductCodeRow);

									} else {
										listEmployeeBulkUploadsPayslip.add(employeeBulkUploadPayslip);
									}
								} else {
									listEmployeeBulkUploadsPayslip.add(employeeBulkUploadPayslip);

								}

							}
						}
					} else {
						return responseCreation(false, "Falied", "Empty file can't be Uploaded", null, null);
					}
				} else {
					return responseCreation(false, "Falied", "Invalid Template", null, null);
				}

				if (finalErrorMessageList.isEmpty()) {
					if (listEmployeeBulkUploadsPayslip.size() > 0) {
						for (EmployeesBulkUploadPayslip upload : listEmployeeBulkUploadsPayslip) {
							PaySlip employeePaySlip = upload.getPaySlip();
							if (employeePaySlip.getId() == null) {
								paySlipService.addPaySlip(employeePaySlip);

							} else {
								paySlipService.addPaySlip(employeePaySlip);
							}
						}
						return responseCreation(true, "Success", "User is saved successfully", null, null);
					} else {
						finalErrorMessageList.add("Employee Details is null");
						finalErrorMessageList.add("Save the employee details");
						return responseCreation(false, "Falied",
								"Employee Detaile is null & Save the employee detaile ", null, finalErrorMessageList);
					}

				} else {
					return responseCreation(false, "Falied", "Uploaded sheet not be null ", null,
							finalErrorMessageList);
				}

			}

			return responseCreation(false, "Falied", "Can't Save Null Values", null, finalErrorMessageList);

		}

		return responseCreation(false, "Failed", "User is null", null, null);
	}

	@SuppressWarnings("null")
	private EmployeesBulkUploadPayslip constructEmployeeFromExcel(Row row, String username, Long id) {
		if (username != null) {

			EmployeesBulkUploadPayslip employeeBulkUploadPayslip = new EmployeesBulkUploadPayslip();

			PaySlip paySlip = new PaySlip();

			MonthAndYear monthAndYear = monthAndYearService.getMonthById(id);

			System.out.println(monthAndYear);

			boolean error = false;
			List<String> listErrorMessage = new ArrayList<>();
			if (row != null) {
//				int index = row.getRowNum() + 1;
				int index = row.getRowNum();

				User employeeAssociateId = null;
//				String employeeGross = null;
				String employeeBasic = null;
				String employeeHRA = null;
				String employeeLTA = null;
				String employeeMedicalAll = null;
				String employeeConveyance = null;
				String employeeSplAllow = null;
				String employeeGrossEarning = null;
				String employeeDaysMonth = null;
				String employeeDaysWorked = null;
				String employeeLop = null;
				String employeeEpfGross = null;
				String employeePf = null;/* PF */
//				String employeeCTC = null;
				String employeePfEmployerContribution = null;
				String employeeTDS = null;
				String employeeMess = null;
				String employeeHostel = null;
				String employeeSBF = null;
				String employeeAdvanceLoan = null;
				String employeeCityAllowance = null;
				String employeeInsurance = null;
				String employeeTotalDeduction = null;
				String employeeNetPay = null;
				String employeePart1 = null;
				String employeePart2 = null;
				String employeeAmount = null;

				// error Print
				String employeeAssciateIdError = null;

				HSSFCell cellemployeeAssociateId = (HSSFCell) row.getCell(1);
				if (cellemployeeAssociateId != null) {
					if (cellemployeeAssociateId.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String employeeAssociateIdCheck = cellemployeeAssociateId.getRichStringCellValue().getString()
								.trim();

						employeeAssciateIdError = cellemployeeAssociateId.getRichStringCellValue().getString().trim();

						String s = employeeAssociateIdCheck;
						long employeeAssociateId1 = Long.parseLong(s);

						employeeAssociateId = userService.getByAssociateId(employeeAssociateIdCheck);

					} else if (cellemployeeAssociateId.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						String employeeAssociateIdCheck = formatter.formatCellValue(cellemployeeAssociateId).trim();
						employeeAssciateIdError = formatter.formatCellValue(cellemployeeAssociateId).trim();
						String s = employeeAssociateIdCheck;
						long employeeAssociateId1 = Long.parseLong(s);

						employeeAssociateId = userService.getByAssociateId(employeeAssociateIdCheck);
					}
				}

//				HSSFCell celllemployeeGross = (HSSFCell) row.getCell(1);
//				if (celllemployeeGross != null) {
//					if (celllemployeeGross.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeGross = celllemployeeGross.getRichStringCellValue().getString().trim();
//
//					} else if (celllemployeeGross.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeGross = formatter.formatCellValue(celllemployeeGross).trim();
//
//					} else if (celllemployeeGross.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeGross = formatter.formatCellValue(celllemployeeGross).trim();
//
//					}
//				}

				HSSFCell cellemployeeBasic = (HSSFCell) row.getCell(8);
				if (cellemployeeBasic != null) {
					if (cellemployeeBasic.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeBasic = cellemployeeBasic.getRichStringCellValue().getString().trim();
//							error = true;
//							listErrorMessage.add( "Invalid Longitude " + celllongitude.getStringCellValue() + " defined in row " + index );
					} else if (cellemployeeBasic.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double employeeBasic1 = cellemployeeBasic.getNumericCellValue();
						employeeBasic = String.valueOf(employeeBasic1).trim();
//						error = true;
//						listErrorMessage.add("Invalid Longitude " + cellemployeeLastname.getStringCellValue()
//								+ " defined in row " + index);
					} else if (cellemployeeBasic.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeBasic = formatter.formatCellValue(cellemployeeBasic).trim();

					} else {
						employeeBasic = "0";
					}
				} else {
					employeeBasic = "0";
				}

				HSSFCell cellemployeeHRA = (HSSFCell) row.getCell(9);
				if (cellemployeeHRA != null) {
					if (cellemployeeHRA.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeHRA = cellemployeeHRA.getRichStringCellValue().getString().trim();
					} else if (cellemployeeHRA.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeHRA11 = cellemployeeHRA.getNumericCellValue();
						employeeHRA = String.valueOf(cellemployeeHRA11);

					} else if (cellemployeeHRA.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeHRA = formatter.formatCellValue(cellemployeeHRA).trim();
					} else {
						employeeHRA = "0";
					}
				} else {
					employeeHRA = "0";
				}

				HSSFCell cellemployeeLTA = (HSSFCell) row.getCell(10);
				if (cellemployeeLTA != null) {
					if (cellemployeeLTA.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeLTA = cellemployeeLTA.getRichStringCellValue().getString().trim();
					} else if (cellemployeeLTA.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeLTA = formatter.formatCellValue(cellemployeeLTA).trim();
					} else if (cellemployeeLTA.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeLTA1 = cellemployeeLTA.getNumericCellValue();
						employeeLTA = String.valueOf(cellemployeeLTA1).trim();

					} else {
						employeeLTA = "0";
					}
				} else {
					employeeLTA = "0";
				}

				HSSFCell cellemployeeMedicalAll = (HSSFCell) row.getCell(11);
				if (cellemployeeMedicalAll != null) {
					if (cellemployeeMedicalAll.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeMedicalAll = cellemployeeMedicalAll.getRichStringCellValue().getString().trim();
					} else if (cellemployeeMedicalAll.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeMedicalAll = formatter.formatCellValue(cellemployeeMedicalAll).trim();
					} else if (cellemployeeMedicalAll.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeMedicalAll1 = cellemployeeMedicalAll.getNumericCellValue();
						employeeMedicalAll = String.valueOf(cellemployeeMedicalAll1).trim();

					} else {
						employeeMedicalAll = "0";
					}
				} else {
					employeeMedicalAll = "0";
				}

				HSSFCell cellemployeeConveyance = (HSSFCell) row.getCell(12);
				if (cellemployeeConveyance != null) {
					if (cellemployeeConveyance.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeConveyance = cellemployeeConveyance.getRichStringCellValue().getString().trim();
					} else if (cellemployeeConveyance.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeConveyance = formatter.formatCellValue(cellemployeeConveyance).trim();
					} else if (cellemployeeConveyance.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeConveyance1 = cellemployeeConveyance.getNumericCellValue();
						employeeConveyance = String.valueOf(cellemployeeConveyance1).trim();

					} else {
						employeeConveyance = "0";
					}
				} else {
					employeeConveyance = "0";
				}

				HSSFCell cellemployeeSplAllow = (HSSFCell) row.getCell(13);
				if (cellemployeeSplAllow != null) {
					if (cellemployeeSplAllow.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeSplAllow = cellemployeeSplAllow.getRichStringCellValue().getString().trim();
					} else if (cellemployeeSplAllow.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeSplAllow = formatter.formatCellValue(cellemployeeSplAllow).trim();
					} else if (cellemployeeSplAllow.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeSplAllow1 = cellemployeeSplAllow.getNumericCellValue();
						employeeSplAllow = String.valueOf(cellemployeeSplAllow1).trim();

					} else {
						employeeSplAllow = "0";
					}
				} else {
					employeeSplAllow = "0";
				}

				HSSFCell cellemployeeGrossEarning = (HSSFCell) row.getCell(14);
				if (cellemployeeGrossEarning != null) {
					if (cellemployeeGrossEarning.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeGrossEarning = cellemployeeGrossEarning.getRichStringCellValue().getString().trim();
					} else if (cellemployeeGrossEarning.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeGrossEarning = formatter.formatCellValue(cellemployeeGrossEarning).trim();
					} else if (cellemployeeGrossEarning.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeGrossEarning1 = cellemployeeGrossEarning.getNumericCellValue();
						employeeGrossEarning = String.valueOf(cellemployeeGrossEarning1).trim();

					} else {
						employeeGrossEarning = "0";
					}
				} else {
					employeeGrossEarning = "0";
				}
				/**** DaysMonth ****/
//				HSSFCell cellemployeeDaysMonth = (HSSFCell) row.getCell(15);
//				if (cellemployeeDaysMonth != null) {
//					if (cellemployeeDaysMonth.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeDaysMonth = cellemployeeDaysMonth.getRichStringCellValue().getString().trim();
//					} else if (cellemployeeDaysMonth.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeDaysMonth = formatter.formatCellValue(cellemployeeDaysMonth).trim();
//					} else if (cellemployeeDaysMonth.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeeDaysMonth1 = cellemployeeDaysMonth.getNumericCellValue();
//						employeeDaysMonth = String.valueOf(cellemployeeDaysMonth1).trim();
//
//					}
//				}

				HSSFCell cellemployeeDaysWorked = (HSSFCell) row.getCell(16);
				if (cellemployeeDaysWorked != null) {
					if (cellemployeeDaysWorked.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeDaysWorked = cellemployeeDaysWorked.getRichStringCellValue().getString().trim();
					} else if (cellemployeeDaysWorked.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeDaysWorked = formatter.formatCellValue(cellemployeeDaysWorked).trim();
					} else if (cellemployeeDaysWorked.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeDaysWorked1 = cellemployeeDaysWorked.getNumericCellValue();
						employeeDaysWorked = String.valueOf(cellemployeeDaysWorked1).trim();

					} else {
						employeeDaysWorked = "0";
					}
				} else {
					employeeDaysWorked = "0";
				}

				HSSFCell cellemployeePf = (HSSFCell) row.getCell(26);
				if (cellemployeePf != null) {
					if (cellemployeePf.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeePf = cellemployeePf.getRichStringCellValue().getString().trim();
					} else if (cellemployeePf.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeePf = formatter.formatCellValue(cellemployeePf).trim();
					} else if (cellemployeePf.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeePf1 = cellemployeePf.getNumericCellValue();
						employeePf = String.valueOf(cellemployeePf1).trim();

					} else {
						employeePf = "0";
					}
				} else {
					employeePf = "0";
				}

				HSSFCell cellemployeeTDS = (HSSFCell) row.getCell(29);
				if (cellemployeeTDS != null) {
					if (cellemployeeTDS.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeTDS = cellemployeeTDS.getRichStringCellValue().getString().trim();
					} else if (cellemployeeTDS.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeTDS = formatter.formatCellValue(cellemployeeTDS).trim();
					} else if (cellemployeeTDS.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeTDS1 = cellemployeeTDS.getNumericCellValue();
						employeeTDS = String.valueOf(cellemployeeTDS1).trim();

					} else {
						employeeTDS = "0";
					}
				} else {
					employeeTDS = "0";
				}

				HSSFCell cellemployeeMess = (HSSFCell) row.getCell(30);
				if (cellemployeeMess != null) {
					if (cellemployeeMess.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeMess = cellemployeeMess.getRichStringCellValue().getString().trim();
					} else if (cellemployeeMess.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeMess = formatter.formatCellValue(cellemployeeMess).trim();
					} else if (cellemployeeMess.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeMess1 = cellemployeeMess.getNumericCellValue();
						employeeMess = String.valueOf(cellemployeeMess1).trim();

					} else {
						employeeMess = "0";
					}
				} else {
					employeeMess = "0";
				}

				HSSFCell cellemployeeHostel = (HSSFCell) row.getCell(31);
				if (cellemployeeHostel != null) {
					if (cellemployeeHostel.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeHostel = cellemployeeHostel.getRichStringCellValue().getString().trim();
					} else if (cellemployeeHostel.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeHostel = formatter.formatCellValue(cellemployeeHostel).trim();
					} else if (cellemployeeHostel.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeHostel1 = cellemployeeHostel.getNumericCellValue();
						employeeHostel = String.valueOf(cellemployeeHostel1).trim();

					} else {
						employeeHostel = "0";
					}
				} else {
					employeeHostel = "0";
				}

				HSSFCell cellemployeeSBF = (HSSFCell) row.getCell(32);
				if (cellemployeeSBF != null) {
					if (cellemployeeSBF.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeSBF = cellemployeeSBF.getRichStringCellValue().getString().trim();
					} else if (cellemployeeSBF.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeSBF = formatter.formatCellValue(cellemployeeSBF).trim();
					} else if (cellemployeeSBF.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeSBF1 = cellemployeeSBF.getNumericCellValue();
						employeeSBF = String.valueOf(cellemployeeSBF1).trim();

					} else {
						employeeSBF = "0";
					}
				} else {
					employeeSBF = "0";
				}
				// Sal advance
				HSSFCell cellemployeeAdvanceLoan = (HSSFCell) row.getCell(33);
				if (cellemployeeAdvanceLoan != null) {
					if (cellemployeeAdvanceLoan.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeAdvanceLoan = cellemployeeAdvanceLoan.getRichStringCellValue().getString().trim();
					} else if (cellemployeeAdvanceLoan.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeAdvanceLoan = formatter.formatCellValue(cellemployeeAdvanceLoan).trim();
					} else if (cellemployeeAdvanceLoan.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeAdvanceLoan1 = cellemployeeAdvanceLoan.getNumericCellValue();
						employeeAdvanceLoan = String.valueOf(cellemployeeAdvanceLoan1).trim();

					} else {
						employeeAdvanceLoan = "0";
					}
				} else {
					employeeAdvanceLoan = "0";
				}

				HSSFCell cellemployeeCityAllowance = (HSSFCell) row.getCell(34);
				if (cellemployeeCityAllowance != null) {
					if (cellemployeeCityAllowance.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeCityAllowance = cellemployeeCityAllowance.getRichStringCellValue().getString().trim();
					} else if (cellemployeeCityAllowance.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeCityAllowance = formatter.formatCellValue(cellemployeeCityAllowance).trim();
					} else if (cellemployeeCityAllowance.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeInsurance1 = cellemployeeCityAllowance.getNumericCellValue();
						employeeCityAllowance = String.valueOf(cellemployeeInsurance1).trim();

					} else {
						employeeCityAllowance = "0";
					}
				} else {
					employeeCityAllowance = "0";
				}

				HSSFCell cellemployeeInsurance = (HSSFCell) row.getCell(35);
				if (cellemployeeInsurance != null) {
					if (cellemployeeInsurance.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeInsurance = cellemployeeInsurance.getRichStringCellValue().getString().trim();
					} else if (cellemployeeInsurance.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeInsurance = formatter.formatCellValue(cellemployeeInsurance).trim();
					} else if (cellemployeeInsurance.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeInsurance1 = cellemployeeInsurance.getNumericCellValue();
						employeeInsurance = String.valueOf(cellemployeeInsurance1).trim();

					} else {
						employeeInsurance = "0";
					}
				} else {
					employeeInsurance = "0";
				}

//				HSSFCell cellemployeeCTC = (HSSFCell) row.getCell(19);
//				if (cellemployeeCTC != null) {
//					if (cellemployeeCTC.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeCTC = cellemployeeCTC.getRichStringCellValue().getString().trim();
//					} else if (cellemployeeCTC.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeCTC = formatter.formatCellValue(cellemployeeCTC).trim();
//					} else if (cellemployeeCTC.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeeCTC1 = cellemployeeCTC.getNumericCellValue();
//						employeeCTC = String.valueOf(cellemployeeCTC1).trim();
//
//					}
//				}

				HSSFCell cellemployeeTotalDeduction = (HSSFCell) row.getCell(36);
				if (cellemployeeTotalDeduction != null) {
					if (cellemployeeTotalDeduction.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeTotalDeduction = cellemployeeTotalDeduction.getRichStringCellValue().getString().trim();
					} else if (cellemployeeTotalDeduction.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeTotalDeduction = formatter.formatCellValue(cellemployeeTotalDeduction).trim();
					} else if (cellemployeeTotalDeduction.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeTotalDeduction1 = cellemployeeTotalDeduction.getNumericCellValue();
						employeeTotalDeduction = String.valueOf(cellemployeeTotalDeduction1).trim();

					} else {
						employeeTotalDeduction = "0";
					}
				} else {
					employeeTotalDeduction = "0";
				}

				HSSFCell cellemployeeNetPay = (HSSFCell) row.getCell(37);
				if (cellemployeeNetPay != null) {
					if (cellemployeeNetPay.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeNetPay = cellemployeeNetPay.getRichStringCellValue().getString().trim();
					} else if (cellemployeeNetPay.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeNetPay = formatter.formatCellValue(cellemployeeNetPay).trim();
					} else if (cellemployeeNetPay.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeNetPay1 = cellemployeeNetPay.getNumericCellValue();
						employeeNetPay = String.valueOf(cellemployeeNetPay1).trim();

					} else {
						employeeNetPay = "0";
					}
				} else {
					employeeNetPay = "0";
				}

				HSSFCell cellemployeeLop = (HSSFCell) row.getCell(43);
				if (cellemployeeLop != null) {
					if (cellemployeeLop.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeLop = cellemployeeLop.getRichStringCellValue().getString().trim();
					} else if (cellemployeeLop.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeLop = formatter.formatCellValue(cellemployeeLop).trim();
					} else if (cellemployeeLop.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
						double cellemployeeLop1 = cellemployeeLop.getNumericCellValue();
						employeeLop = String.valueOf(cellemployeeLop1).trim();

					} else {
						employeeLop = "0";
					}
				} else {
					employeeLop = "0";
				}

//				HSSFCell cellemployeePfEmployerContribution = (HSSFCell) row.getCell(15);
//				if (cellemployeePfEmployerContribution != null) {
//					if (cellemployeePfEmployerContribution.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeePfEmployerContribution = cellemployeePfEmployerContribution.getRichStringCellValue()
//								.getString();
//					} else if (cellemployeePfEmployerContribution.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeePfEmployerContribution = formatter.formatCellValue(cellemployeePfEmployerContribution);
//					}else if (cellemployeePfEmployerContribution.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeePfEmployerContribution1=cellemployeePfEmployerContribution.getNumericCellValue();
//						employeePfEmployerContribution = String.valueOf(cellemployeePfEmployerContribution1);  
//
//					}
//				}
//
//				HSSFCell cellemployeeCityAllowance = (HSSFCell) row.getCell(21);
//				if (cellemployeeCityAllowance != null) {
//					if (cellemployeeCityAllowance.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeCityAllowance = cellemployeeCityAllowance.getRichStringCellValue().getString();
//					} else if (cellemployeeCityAllowance.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeCityAllowance = formatter.formatCellValue(cellemployeeCityAllowance);
//					}else if (cellemployeeCityAllowance.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeeCityAllowance1=cellemployeeCityAllowance.getNumericCellValue();
//						employeeCityAllowance = String.valueOf(cellemployeeCityAllowance1);  
//
//					}
//				}
//				
//				HSSFCell cellemployeePart1 = (HSSFCell) row.getCell(25);
//				if (cellemployeePart1 != null) {
//					if (cellemployeePart1.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeePart1 = cellemployeePart1.getRichStringCellValue().getString();
//					} else if (cellemployeePart1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeePart1 = formatter.formatCellValue(cellemployeePart1);
//					}else if (cellemployeePart1.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeePart11=cellemployeePart1.getNumericCellValue();
//						employeePart1 = String.valueOf(cellemployeePart11);  
//
//					}
//				}
//
//				HSSFCell cellemployeePart2 = (HSSFCell) row.getCell(26);
//				if (cellemployeePart2 != null) {
//					if (cellemployeePart2.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeePart2 = cellemployeePart2.getRichStringCellValue().getString();
//					} else if (cellemployeePart2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeePart2 = formatter.formatCellValue(cellemployeePart2);
//					}else if (cellemployeePart2.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeePart21=cellemployeePart2.getNumericCellValue();
//						employeePart2 = String.valueOf(cellemployeePart21);  
//
//					}
//				}
//
//				HSSFCell cellemployeeAmount = (HSSFCell) row.getCell(27);
//				if (cellemployeeAmount != null) {
//					if (cellemployeeAmount.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeAmount = cellemployeeAmount.getRichStringCellValue().getString();
//					} else if (cellemployeeAmount.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeAmount = formatter.formatCellValue(cellemployeeAmount);
//					}else if (cellemployeeAmount.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
//						double cellemployeeAmount1=cellemployeeAmount.getNumericCellValue();
//						employeeAmount = String.valueOf(cellemployeeAmount1);  
//
//					}
//					
//				}

				if (employeeAssociateId == null) {
//					error = true;
					listErrorMessage
							.add("This AssociateId is " + employeeAssciateIdError + " not saved in row " + index);

				} else {
//					String s = employeeAssociateId;
//					long employeeAssociateId1 = Long.parseLong(s);

//					PaySlip employeeAssociateIdCheck = paySlipService
//							.findByAssociateId(employeeAssociateId.getAssociateId());
					PaySlip employeeAssociateIdCheck = paySlipService
							.findByAssociateIdAndMonthId(employeeAssociateId.getAssociateId(), id);
					if (employeeAssociateIdCheck != null) {
						error = true;
						listErrorMessage.add(employeeAssociateId.getAssociateId() + "  and Month of "
								+ employeeAssociateIdCheck.getMonthAndYear().getMonth().getMonth() + " in row " + index
								+ " already exists. ");
					}
				}

//				if (employeeGross == null || employeeGross.isEmpty()) {
//					error = true;
//						listErrorMessage.add( hierarchyHeader.getLevel1Name()+" is empty in row  " + index );
//				}

				if (employeeBasic == null || employeeBasic.isEmpty()) {
//					error = true;
					listErrorMessage.add("Basic is empty in row  " + index);
				}
				if (employeeHRA == null || employeeHRA.isEmpty()) {
//					error = true;
					listErrorMessage.add("HRA is empty in row  " + index);
				}

				if (employeeLTA == null || employeeLTA.isEmpty()) {
//					error = true;
					listErrorMessage.add("LTA is empty in row  " + index);
				}

				if (employeeMedicalAll == null || employeeMedicalAll.isEmpty()) {
//					error = true;
					listErrorMessage.add("MedicalAll  is empty in row  " + index);
				}

				if (employeeConveyance == null || employeeConveyance.isEmpty()) {
//					error = true;
					listErrorMessage.add("Conveyance is empty in row  " + index);
				}
				if (employeeSplAllow == null || employeeSplAllow.isEmpty()) {
//					error = true;
					listErrorMessage.add("Spl Allow is empty in row  " + index);
				}

				if (employeeGrossEarning == null || employeeGrossEarning.isEmpty()) {
//					error = true;
					listErrorMessage.add("Gross Earning is empty in row  " + index);
				}
//				if (employeeDaysMonth == null || employeeDaysMonth.isEmpty()) {
////						listErrorMessage.add( hierarchyHeader.getLevel1Name()+" is empty in row  " + index );
//				}

				if (employeeDaysWorked == null || employeeDaysWorked.isEmpty()) {
//					error = true;
					listErrorMessage.add("DaysWorked is empty in row  " + index);
				}
				if (employeeLop == null || employeeLop.isEmpty()) {
//					error = true;
					listErrorMessage.add("Lop is empty in row  " + index);
				}

				if (employeePf == null || employeePf.isEmpty()) {
//					error = true;
					listErrorMessage.add("PF is empty in row  " + index);
				}
				if (employeeTDS == null || employeeTDS.isEmpty()) {
//					error = true;
					listErrorMessage.add("TDS is empty in row  " + index);
				}

				if (employeeMess == null || employeeMess.isEmpty()) {
//					error = true;
					listErrorMessage.add("Mess is empty in row  " + index);
				}

				if (employeeHostel == null || employeeHostel.isEmpty()) {
//					error = true;
					listErrorMessage.add("Hostel is empty in row  " + index);
				}
				if (employeeSBF == null || employeeSBF.isEmpty()) {
//					error = true;
					listErrorMessage.add(" SBF is empty in row  " + index);
				}

				if (employeeAdvanceLoan == null || employeeAdvanceLoan.isEmpty()) {
//					error = true;
					listErrorMessage.add("AdvanceLoan is empty in row  " + index);
				}
				if (employeeCityAllowance == null || employeeCityAllowance.isEmpty()) {
//					error = true;
					listErrorMessage.add("City Allowance is empty in row  " + index);
				}

				if (employeeInsurance == null || employeeInsurance.isEmpty()) {
//					error = true;
					listErrorMessage.add("Insurance is empty in row  " + index);
				}

//				if (employeeCTC == null || employeeCTC.isEmpty()) {
//					error = true;
//						listErrorMessage.add( hierarchyHeader.getLevel1Name()+" is empty in row  " + index );
//				}

				if (employeeTotalDeduction == null || employeeTotalDeduction.isEmpty()) {
//					error = true;
					listErrorMessage.add("Total Deduction is empty in row  " + index);

				}

				if (employeeNetPay == null || employeeNetPay.isEmpty()) {
//					error = true;
					listErrorMessage.add("Net Pay is empty in row  " + index);

				}

				if (monthAndYear == null || monthAndYear.getId() == null) {
//					error = true;
					listErrorMessage.add("Month id is null" + index);
				}

				if (listErrorMessage.isEmpty()) {

//					Long userRole = (long) 2;
//
//					Optional<Role> role = roleService.getById(userRole);

//					String assIdValue = employeeAssociateId;
//					long assciateIdvalue = Long.parseLong(assIdValue);
					paySlip.setId(null);
					paySlip.setUser(employeeAssociateId);
					paySlip.setAdvanceLoan(employeeAdvanceLoan);
					paySlip.setBasic(employeeBasic);
					paySlip.setConveyance(employeeConveyance);
//					paySlip.setcTC(employeeCTC);
					paySlip.setDaysMonth(employeeDaysMonth);
					paySlip.setDaysWorked(employeeDaysWorked);

					paySlip.setGrossEarning(employeeGrossEarning);
					paySlip.setHostel(employeeHostel);
					paySlip.sethRA(employeeHRA);
					paySlip.setInsurance(employeeInsurance);
					paySlip.setLop(employeeLop);
					paySlip.setlTA(employeeLTA);
					paySlip.setMedicalAll(employeeMedicalAll);
					paySlip.setMess(employeeMess);
					paySlip.setCityAllowance(employeeCityAllowance);
					paySlip.setNetPay(employeeNetPay);
					paySlip.setPf(employeePf);
					paySlip.setsBF(employeeSBF);
					paySlip.setSplAllow(employeeSplAllow);
					paySlip.settDS(employeeTDS);
					paySlip.setTotalDeduction(employeeTotalDeduction);
					paySlip.setMonthAndYear(monthAndYear);
					
					paySlip.setCreatedBy("ADMIN");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					paySlip.setCreatedDate(sdf1.format(new Date()));

				} else {

					error = true;
//						listErrorMessage.add("Empty row");

				}

			}
			employeeBulkUploadPayslip.setError(error);
			employeeBulkUploadPayslip.setErrorMessage(listErrorMessage);

			if (paySlip.getUser() != null) {
				employeeBulkUploadPayslip.setPaySlip(paySlip);
			} else {

			}

			return employeeBulkUploadPayslip;

		}
		return null;
	}

	private boolean validatePaySlipDataUpload(Row row) {
		boolean validateTemplateFlag = true;
		if (row == null) {
			return validateTemplateFlag = false;
		} else {
			try {
//				String empSerialNo = row.getCell(0).getRichStringCellValue().getString();
				String empId = row.getCell(1).getRichStringCellValue().getString();
//				String empName = row.getCell(2).getRichStringCellValue().getString();
//				String empDesignation = row.getCell(3).getRichStringCellValue().getString();
//				String empGender = row.getCell(4).getRichStringCellValue().getString();
//				String empUnit = row.getCell(5).getRichStringCellValue().getString();
//				String empDOJ = row.getCell(6).getRichStringCellValue().getString();
//				String empGross = row.getCell(7).getRichStringCellValue().getString();
				String basic = row.getCell(8).getRichStringCellValue().getString();
				String hra = row.getCell(9).getRichStringCellValue().getString();
				String lta = row.getCell(10).getRichStringCellValue().getString();
				String medicalAll = row.getCell(11).getRichStringCellValue().getString();
				String conveyance = row.getCell(12).getRichStringCellValue().getString();
				String splAllow = row.getCell(13).getRichStringCellValue().getString();
				String grossEarning = row.getCell(14).getRichStringCellValue().getString();
//				String daysMonth = row.getCell(15).getRichStringCellValue().getString();
				String daysWorked = row.getCell(16).getRichStringCellValue().getString();
//				String daysAbsent = row.getCell(17).getRichStringCellValue().getString();
//				String basic_1 = row.getCell(18).getRichStringCellValue().getString();
//				String hra_1 = row.getCell(19).getRichStringCellValue().getString();
//				String lta_1 = row.getCell(20).getRichStringCellValue().getString();
//				String medicalAll_1 = row.getCell(21).getRichStringCellValue().getString();
//				String conveyance_1 = row.getCell(22).getRichStringCellValue().getString();
//				String splAllow_1 = row.getCell(23).getRichStringCellValue().getString();
//				String epfGross = row.getCell(24).getRichStringCellValue().getString();
//				String grossEarning_1 = row.getCell(25).getRichStringCellValue().getString();
				String pf = row.getCell(26).getRichStringCellValue().getString();
//				String ctc = row.getCell(27).getRichStringCellValue().getString();
//				String pfEmployerContr = row.getCell(28).getRichStringCellValue().getString();
				String tds = row.getCell(29).getRichStringCellValue().getString();
				String mess = row.getCell(30).getRichStringCellValue().getString();
				String hostel = row.getCell(31).getRichStringCellValue().getString();
				String sbf = row.getCell(32).getRichStringCellValue().getString();
				String advanceLoan = row.getCell(33).getRichStringCellValue().getString();
				String cityAllowance = row.getCell(34).getRichStringCellValue().getString();
				String insurance = row.getCell(35).getRichStringCellValue().getString();
				String totalDeduction = row.getCell(36).getRichStringCellValue().getString();
				String netPay = row.getCell(37).getRichStringCellValue().getString();
//				String bankMode = row.getCell(38).getRichStringCellValue().getString();
//				String bankAccount = row.getCell(39).getRichStringCellValue().getString();
//				String part1 = row.getCell(40).getRichStringCellValue().getString();
//				String part2 = row.getCell(41).getRichStringCellValue().getString();
//				String amount = row.getCell(42).getRichStringCellValue().getString();
				String lop = row.getCell(43).getRichStringCellValue().getString();

//				if (!(empSerialNo.trim()).equals("S.No")) {
//					return validateTemplateFlag = false;
//				}
				if (!(empId.trim()).equals("Associate Id")) {
					return validateTemplateFlag = false;
				}
//				if (!(empName.trim()).equals("Emp Name")) {
//					return validateTemplateFlag = false;
//				}				
//				if (!(empDesignation.trim()).equals("Designation")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(empGender.trim()).equals("Gender")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(empUnit.trim()).equals("Unit")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(empDOJ.trim()).equals("D.O.J")) {
//					return validateTemplateFlag = false;
//				}
//								
//				if (!(empGross.trim()).equals("Gross")) {
//					return validateTemplateFlag = false;
//				}
				if (!(basic.trim()).equals("Basic")) {
					return validateTemplateFlag = false;
				}
				if (!(hra.trim()).equals("HRA")) {
					return validateTemplateFlag = false;
				}
				if (!(lta.trim()).equals("LTA")) {
					return validateTemplateFlag = false;
				}
				if (!(medicalAll.trim()).equals("Medical All")) {
					return validateTemplateFlag = false;
				}
				if (!(conveyance.trim()).equals("Conveyance")) {
					return validateTemplateFlag = false;
				}
				if (!(splAllow.trim()).equals("Spl allow")) {
					return validateTemplateFlag = false;
				}
				if (!(grossEarning.trim()).equals("Gross Earnings")) {
					return validateTemplateFlag = false;
				}
//				if (!(daysMonth.trim()).equals("#days/  month")) {
//					return validateTemplateFlag = false;
//				}
				if (!(daysWorked.trim()).equals("#days/  worked")) {
					return validateTemplateFlag = false;
				}
//				if (!(daysAbsent.trim()).equals("# of days Absent")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(basic_1.trim()).equals("Basic")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(hra_1.trim()).equals("HRA")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(lta_1.trim()).equals("LTA")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(medicalAll_1.trim()).equals("Medical All")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(conveyance_1.trim()).equals("Conveyance")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(splAllow_1.trim()).equals("Spl allow")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(epfGross.trim()).equals("epf Gross")) {
//					return validateTemplateFlag = false;
//				}				
//				if (!(grossEarning_1.trim()).equals("Gross Earnings")) {
//					return validateTemplateFlag = false;
//				}
				if (!(pf.trim()).equals("PF")) {
					return validateTemplateFlag = false;
				}
//				if (!(ctc.trim()).equals("CTC")) {
//					return validateTemplateFlag = false;
//				}

//				if (!(pfEmployerContr.trim()).equals("PF Employer Contr 12%")) {
//					return validateTemplateFlag = false;
//				}
				if (!(tds.trim()).equals("TDS")) {
					return validateTemplateFlag = false;
				}
				if (!(mess.trim()).equals("Mess")) {
					return validateTemplateFlag = false;
				}
				if (!(hostel.trim()).equals("Hostel")) {
					return validateTemplateFlag = false;
				}
				if (!(sbf.trim()).equals("SBF")) {
					return validateTemplateFlag = false;
				}
				if (!(advanceLoan.trim()).equals("Sal advance")) {
					return validateTemplateFlag = false;
				}
				if (!(cityAllowance.trim()).equals("City Allowance")) {
					return validateTemplateFlag = false;
				}
				if (!(insurance.trim()).equals("Insurance")) {
					return validateTemplateFlag = false;
				}
				if (!(totalDeduction.trim()).equals("Total deduction")) {
					return validateTemplateFlag = false;
				}
				if (!(netPay.trim()).equals("Net pay 19")) {
					return validateTemplateFlag = false;
				}
//				if (!(bankMode.trim()).equals("Bank Mode")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(bankAccount.trim()).equals("Bank Account")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(part1.trim()).equals("Part I")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(part2.trim()).equals("Part II")) {
//					return validateTemplateFlag = false;
//				}
//				if (!(amount.trim()).equals("Amount")) {
//					return validateTemplateFlag = false;
//				}
				if (!(lop.trim()).equals("LOP")) {
					return validateTemplateFlag = false;
				}

			} catch (NullPointerException e) {
				return validateTemplateFlag = false;
			}
		}
		return validateTemplateFlag;
	}

	private ResponseEntity<Object> responseCreation(boolean status, String message, String description, String filepath,
			List<String> string) {
		// TODO Auto-generated method stub
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (filepath != null) {
			response.setFile(filepath);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "file" });
		} else if (string != null) {
			response.setMessages(string);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "messages" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

}
