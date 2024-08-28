/**
 * 
 */
package com.pay.payslip.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.dto.EmployeeBulkUpload;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.Role;
import com.pay.payslip.model.User;
import com.pay.payslip.servie.RoleService;
import com.pay.payslip.servie.UserService;
import com.pay.payslip.util.FilterUtil;
import com.pay.payslip.util.ResponseModel;

/**
 * @author Leo Navin
 *
 */
@RestController
@RequestMapping(value = "/employeeBulkUpload")
public class EmployeeBulkUploading {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	private ResponseModel response = new ResponseModel();

	private MappingJacksonValue mappingJacksonValue;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

	@GetMapping(value = "/exportEmployeeuploadTemplate")
	public void exportProductuploadTemplate(@RequestHeader("Authorization") String token, HttpServletResponse response)
			throws IOException {
//		String productLabel = principalUtil.getPoductLabel();
//		String productTypeLabel = principalUtil.getProductTypeLabel();

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent() && logInUser.get().getRole().getRole().equals("ADMIN")) {

			Date date = new Date();

			response.setContentType("application/vnd.ms-excel");
			String fileName = "Employee" + " BulkUpload Template" + date.getTime() + ".xls";
			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=\"" + fileName + "\"";

			String pragma = "Pragma";
			String pragmaValue = fileName;

			response.setHeader(headerKey, headerValue);

			response.setHeader(pragma, pragmaValue);

//			String fileName = "Employee" + " BulkUpload Template" + date.getTime() + ".xls";
//			String contextPath = ConstantUtil.CONTEXT_PATH;
//			FileOutputStream fileOutputStream = null;
//			File file = null;
//			try {
//				file = new File(contextPath + fileName);
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//				fileOutputStream = new FileOutputStream(file);
			OutputStream out = response.getOutputStream();

//			} catch (FileNotFoundException ex) {
//				ex.printStackTrace();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}

			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet hssfSheet = hssfWorkbook.createSheet("Employee" + " Details ");
			HSSFRow headerRow = hssfSheet.createRow(0);

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

			HSSFCell associateId = headerRow.createCell(0);
			associateId.setCellValue(/* "Employee" + */ "Associate Id" + "*");
			associateId.setCellStyle(tableHeaderStyle);
			associateId.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell firstname = headerRow.createCell(1);
			firstname.setCellValue(/* "Employee" + */ "Associate Name");
			firstname.setCellStyle(tableHeaderStyle);
			firstname.setCellType(Cell.CELL_TYPE_STRING);

//			HSSFCell lastname = headerRow.createCell(2);
//			lastname.setCellValue(/* "Employee" + */ "Last Name");
//			lastname.setCellStyle(tableHeaderStyle);
//			lastname.setCellType(Cell.CELL_TYPE_STRING);

			HSSFPatriarch patr = hssfSheet.createDrawingPatriarch();

			HSSFComment comment3 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 8, (short) 7, 12));
			comment3.setString(new HSSFRichTextString(" \n Date Format : dd-MM-yyyy \n Sample Date : 01-01-2016"));
			
			HSSFCell dateOfBirth = headerRow.createCell(2);
			dateOfBirth.setCellValue(/* "Employee" + */ "Date Of Birth");
			dateOfBirth.setCellStyle(tableHeaderStyle);
			dateOfBirth.setCellComment(comment3);
			dateOfBirth.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell email = headerRow.createCell(3);
			email.setCellValue(/* "Employee" + */ "Email"  + "*");
			email.setCellStyle(tableHeaderStyle);
			email.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell password = headerRow.createCell(4);
			password.setCellValue(/* "Employee" + */ "Password");
			password.setCellStyle(tableHeaderStyle);
			password.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell phoneNumber = headerRow.createCell(5);
			phoneNumber.setCellValue(/* "Employee" + */ "Phone Number");
			phoneNumber.setCellStyle(tableHeaderStyle);
			phoneNumber.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell uan = headerRow.createCell(6);
			uan.setCellValue(/* "Employee" + */"UAN"  + "*");
			uan.setCellStyle(tableHeaderStyle);
			uan.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell designation = headerRow.createCell(7);
			designation.setCellValue(/* "Employee" + */ "Designation");
			designation.setCellStyle(tableHeaderStyle);
			designation.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell gender = headerRow.createCell(8);
			gender.setCellValue(/* "Employee" + */ "Gender");
			gender.setCellStyle(tableHeaderStyle);
			gender.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell unit = headerRow.createCell(9);
			unit.setCellValue(/* "Employee" + */"Unit");
			unit.setCellStyle(tableHeaderStyle);
			unit.setCellType(Cell.CELL_TYPE_STRING);

			HSSFComment comment2 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 8, (short) 7, 12));
			comment2.setString(new HSSFRichTextString(" \n Date Format : dd-MM-yyyy \n Sample Date : 01-01-2016"));
			
			HSSFCell dateOfJoining = headerRow.createCell(10);
			dateOfJoining.setCellValue(/* "Employee" + */ "Date Of Joining");
			dateOfJoining.setCellStyle(tableHeaderStyle);
			dateOfJoining.setCellComment(comment2);
			dateOfJoining.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell bankName = headerRow.createCell(11);
			bankName.setCellValue(/* "Employee" + */ "Bank Name"  + "*");
			bankName.setCellStyle(tableHeaderStyle);
			bankName.setCellType(Cell.CELL_TYPE_STRING);

			HSSFCell bankAccountNumber = headerRow.createCell(12);
			bankAccountNumber.setCellValue(/* "Employee" + */ "Bank Account Number"  + "*");
			bankAccountNumber.setCellStyle(tableHeaderStyle);
			bankAccountNumber.setCellType(Cell.CELL_TYPE_STRING);

//			HSSFCell username = headerRow.createCell(12);
//			username.setCellValue(/* "Employee" + */" Username " + "*");
//			username.setCellStyle(tableHeaderStyle);
//			username.setCellType(Cell.CELL_TYPE_STRING);

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
//			hssfSheet.autoSizeColumn(13);

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

	@PostMapping(value = "/uploadBulkEmployee")
	public ResponseEntity<Object> uploadBulkEmployee(MultipartHttpServletRequest re,
			@RequestHeader("Authorization") String token) throws IOException, ParseException {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent() && logInUser.get().getRole().getRole().equals("ADMIN")) {

			EmployeeBulkUpload employeeBulkUpload = new EmployeeBulkUpload();
			Set<String> UniqueEmployeeAssociateIdList = new HashSet<String>();
			Set<User> UniqueEmployeeUsernameList = new HashSet<User>();
			Set<String> UniqueEmployeeUANList = new HashSet<String>();
			Set<String> UniqueEmployeeBankAccountNumberList = new HashSet<String>();
			Set<String> UniqueEmployeeEmailList = new HashSet<String>();

			MultipartFile file = re.getFile("file");
			Workbook workbook = null;
			FileInputStream fileInputStream = null;
			ByteArrayInputStream byteArrayInputStream = null;

			List<EmployeeBulkUpload> listEmployeeBulkUploads = new ArrayList<EmployeeBulkUpload>();
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
				boolean templateValidationFlag = validateEmployeeDataUpload(sheet.getRow(0));

				if (templateValidationFlag) {

					if (lastRowNum != 0) {
						for (int j = 1; j <= lastRowNum; j++) {
							row = sheet.getRow(j);

							employeeBulkUpload = constructEmployeeFromExcel(row, logInUser.get().getUsername());

							if (employeeBulkUpload != null) {
								if (employeeBulkUpload.getErrorMessage().size()>0) {
									List<String> message = employeeBulkUpload.getErrorMessage();

									finalErrorMessageList.addAll(message);

//							return responseCreation(false, "Failed", null,null, message);

								} else if (employeeBulkUpload.getUser() != null) {
									if (!UniqueEmployeeAssociateIdList
											.add(employeeBulkUpload.getUser().getAssociateId())) {
										int duplicateProductCodeRow = j + 1;
										String message = "Duplicate " + "Employee " +employeeBulkUpload.getUser().getAssociateId() + " Code in row : ";

										finalErrorMessageList.add(message + " " + duplicateProductCodeRow);
//								return responseCreation(false, "Failed", null,message1 +" "+duplicateProductCodeRow,null);

									}
//									else if (!UniqueEmployeeUsernameList.add(employeeBulkUpload.getUser())) {
//										int duplicateProductNameRow = j + 1;
//										String message = "Duplicate  " + "Employee User "+employeeBulkUpload.getUser() + "in row :";
//
//										finalErrorMessageList.add(message + " " + duplicateProductNameRow);
//
//									} 
									else if (!UniqueEmployeeUANList.add(employeeBulkUpload.getUser().getUan())) {
										int duplicateProductNameRow = j + 1;
										String message = "Duplicate " + employeeBulkUpload.getUser().getUan()
												+ "EmployeeUAN in row :";

										finalErrorMessageList.add(message + " " + duplicateProductNameRow);

//								return responseCreation(false, "Failed",null, message,null);
									} else if (!UniqueEmployeeBankAccountNumberList
											.add(employeeBulkUpload.getUser().getBankAccountNumber())) {
										int duplicateProductNameRow = j + 1;
										String message = "Duplicate  "
												+ employeeBulkUpload.getUser().getBankAccountNumber()
												+ " BankAccountNumber in row : ";

										finalErrorMessageList.add(message + " " + duplicateProductNameRow);
//								return responseCreation(false, "Failed",null, message,null);
									} else if (!UniqueEmployeeEmailList.add(employeeBulkUpload.getUser().getEmail())) {
										int duplicateProductNameRow = j + 1;
										String message = "Duplicate  " + employeeBulkUpload.getUser().getEmail()
												+ " Email in row :";

										finalErrorMessageList.add(message + " " + duplicateProductNameRow);
//								return responseCreation(false, "Failed",null, message,null);
									} else {
										listEmployeeBulkUploads.add(employeeBulkUpload);
									}
								}
							}
						}
					} else {
						return responseCreation(false, "Failed", "Empty file can't be Uploaded", null, null);

					}

				} else {
					return responseCreation(false, "Failed", "Invalid Template", null, null);

				}

				if (finalErrorMessageList.isEmpty()) {
					for (EmployeeBulkUpload upload : listEmployeeBulkUploads) {
						User employee = upload.getUser();
						if (employee.getId() == null) {
							employee.setCreatedBy("ADMIN");
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							employee.setCreatedDate(sdf1.format(new Date()));
							userService.addUser(employee);
						} else {
							userService.addUser(employee);
						}
					}
					return responseCreation(true, "Success", "User is saved successfully", null, null);

				}
				return responseCreation(false, "Failed", null, null, finalErrorMessageList);

			}

			return responseCreation(false, "Failed", "User data can't be null", null, null);
		}
		return responseCreation(false, "Failed", "User can't be authorization token!", null, null);
	}

	@SuppressWarnings("null")
	private EmployeeBulkUpload constructEmployeeFromExcel(Row row, String username) throws ParseException {
		if (username != null) {

			EmployeeBulkUpload employeeBulkUpload = new EmployeeBulkUpload();
			User user = new User();

			boolean error = false;
			List<String> listErrorMessage = new ArrayList<>();
			if (row != null) {
				int index = row.getRowNum() + 1;

				String employeeAssociateId = null;
				String employeeFirstname = null;
//				String employeeLastname = null;
				String employeeDateOfBirth = null;
				String employeeEmail = null;
				String employeePhoneNumber = null;
//				String employeeUsername = null;
				String employeePassword = null;
				String employeeUAN = null;
				String employeeDesignation = null;
				String employeeGender = null;
				String employeeUnit = null;
				String employeeDOJ = null;
				String employeeBankName = null;
				String employeeBankAccountNumber = null;

				HSSFCell cellemployeeAssociateId = (HSSFCell) row.getCell(0);
				if (cellemployeeAssociateId != null) {
					if (cellemployeeAssociateId.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeAssociateId = cellemployeeAssociateId.getRichStringCellValue().getString().trim();
					} else if (cellemployeeAssociateId.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeAssociateId = formatter.formatCellValue(cellemployeeAssociateId).trim();
					}
				}

				HSSFCell celllemployeeFirstname = (HSSFCell) row.getCell(1);
				if (celllemployeeFirstname != null) {
					if (celllemployeeFirstname.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeFirstname = celllemployeeFirstname.getRichStringCellValue().getString().trim();
//							error = true;
//							listErrorMessage.add( "Invalid Latitude " + celllatitude.getStringCellValue() + " defined in row " + index );
					} else if (celllemployeeFirstname.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//							 DataFormatter formatter = new DataFormatter();
//							 latitude = formatter.formatCellValue(celllemployeeFirstname);
						error = true;
						listErrorMessage.add("Invalid Latitude " + celllemployeeFirstname.getStringCellValue()
								+ " defined in row " + index);
					}
				} else {
					employeeFirstname = "-";
				}

//				HSSFCell cellemployeeLastname = (HSSFCell) row.getCell(2);
//				if (cellemployeeLastname != null) {
//					if (cellemployeeLastname.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeLastname = cellemployeeLastname.getRichStringCellValue().getString().trim();
//					} else if (cellemployeeLastname.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						error = true;
//						listErrorMessage.add("Invalid Longitude " + cellemployeeLastname.getStringCellValue()
//								+ " defined in row " + index);
//					}
//				} else {
//					employeeLastname = "-";
//				}

				HSSFCell cellemployeeDateOfBirth = (HSSFCell) row.getCell(2);
				if (cellemployeeDateOfBirth != null) {
					if (cellemployeeDateOfBirth.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
//					    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

						SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

//					    String inputDateStr = "01-Jan-2023";
						Date date = inputFormat.parse(cellemployeeDateOfBirth.getRichStringCellValue().getString().trim());
						employeeDateOfBirth = outputFormat.format(date);

//						employeeDateOfBirth = cellemployeeDateOfBirth.getRichStringCellValue().getString().trim();
					} else if (cellemployeeDateOfBirth.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {

						SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
//					    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

						SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

//					    String inputDateStr = "01-Jan-2023";
						Date date = inputFormat.parse(cellemployeeDateOfBirth.toString());
						employeeDateOfBirth = outputFormat.format(date);

					}
				} else {
					SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

					SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

//				    String inputDateStr = "01-Jan-2023";
//					Date date = inputFormat.parse("-");
//					employeeDateOfBirth = outputFormat.format(date);
					employeeDateOfBirth="-";
				}

				HSSFCell cellemployeeEmail = (HSSFCell) row.getCell(3);
				if (cellemployeeEmail != null) {
					if (cellemployeeEmail.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeEmail = cellemployeeEmail.getRichStringCellValue().getString().trim();
					} else if (cellemployeeEmail.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeEmail = formatter.formatCellValue(cellemployeeEmail).trim();
					}
				}

				HSSFCell cellemployeePassword = (HSSFCell) row.getCell(4);
				if (cellemployeePassword != null) {
					if (cellemployeePassword.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeePassword = cellemployeePassword.getRichStringCellValue().getString().trim();
					} else if (cellemployeePassword.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeePassword = formatter.formatCellValue(cellemployeePassword).trim();
					}
				}

				HSSFCell cellemployeePhoneNumber = (HSSFCell) row.getCell(5);
				if (cellemployeePhoneNumber != null) {
					if (cellemployeePhoneNumber.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeePhoneNumber = cellemployeePhoneNumber.getRichStringCellValue().getString().trim();
					} else if (cellemployeePhoneNumber.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeePhoneNumber = formatter.formatCellValue(cellemployeePhoneNumber).trim();
					}
				} else {
					employeePhoneNumber = "-";
				}

				HSSFCell cellUAN = (HSSFCell) row.getCell(6);
				if (cellUAN != null) {
					if (cellUAN.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeUAN = cellUAN.getRichStringCellValue().getString().trim();
					} else if (cellUAN.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeUAN = formatter.formatCellValue(cellUAN).trim();
					}
				}

				HSSFCell cellEmployeeDesignation = (HSSFCell) row.getCell(7);
				if (cellEmployeeDesignation != null) {
					if (cellEmployeeDesignation.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeDesignation = cellEmployeeDesignation.getRichStringCellValue().getString().trim();
					} else if (cellemployeePhoneNumber.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeDesignation = formatter.formatCellValue(cellEmployeeDesignation).trim();
					}
				} else {
					employeeDesignation = "-";
				}

				HSSFCell cellemployeeGender = (HSSFCell) row.getCell(8);
				if (cellemployeeGender != null) {
					if (cellemployeeGender.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeGender = cellemployeeGender.getRichStringCellValue().getString().trim();
					} else if (cellemployeeGender.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeGender = formatter.formatCellValue(cellemployeeGender).trim();
					}
				} else {
					employeeGender = "-";
				}

				HSSFCell cellemployeeUnit = (HSSFCell) row.getCell(9);
				if (cellemployeeUnit != null) {
					if (cellemployeeUnit.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeUnit = cellemployeeUnit.getRichStringCellValue().getString().trim();
					} else if (cellemployeeUnit.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeUnit = formatter.formatCellValue(cellemployeeUnit).trim();
					}
				} else {
					employeeUnit = "-";
				}

				HSSFCell cellemployeeDOJ = (HSSFCell) row.getCell(10);
				if (cellemployeeDOJ != null) {
					if (cellemployeeDOJ.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
//					    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

						SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

//					    String inputDateStr = "01-Jan-2023";
						Date date = inputFormat.parse(cellemployeeDOJ.getRichStringCellValue().getString().trim());
						employeeDOJ = outputFormat.format(date);
//						employeeDOJ = cellemployeeDOJ.getRichStringCellValue().getString().trim();
					} else if (cellemployeeDOJ.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {

						SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
//					    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

						SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

//					    String inputDateStr = "01-Jan-2023";
						Date date = inputFormat.parse(cellemployeeDOJ.toString());
						employeeDOJ = outputFormat.format(date);

//						DataFormatter formatter = new DataFormatter();
//						employeeDOJ = formatter.formatCellValue(cellemployeeDOJ).trim();

					}
				} else {
					SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//				    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

					SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

//				    String inputDateStr = "01-Jan-2023";
					
//					Date date = inputFormat.parse("-");
//					employeeDOJ = outputFormat.format(date);
					employeeDOJ = "-";
				}

				HSSFCell cellemployeeBankName = (HSSFCell) row.getCell(11);
				if (cellemployeeBankName != null) {
					if (cellemployeeBankName.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeBankName = cellemployeeBankName.getRichStringCellValue().getString().trim();
					} else if (cellemployeeBankName.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeBankName = formatter.formatCellValue(cellemployeeBankName).trim();
					}
				}

				HSSFCell cellemployeeBankAccountNumber = (HSSFCell) row.getCell(12);
				if (cellemployeeBankAccountNumber != null) {
					if (cellemployeeBankAccountNumber.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						employeeBankAccountNumber = cellemployeeBankAccountNumber.getRichStringCellValue().getString()
								.trim();
					} else if (cellemployeeBankAccountNumber.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DataFormatter formatter = new DataFormatter();
						employeeBankAccountNumber = formatter.formatCellValue(cellemployeeBankAccountNumber).trim();
					}
				}

//				HSSFCell cellemployeeUsername = (HSSFCell) row.getCell(12);
//				if (cellemployeeUsername != null) {
//					if (cellemployeeUsername.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//						employeeUsername = cellemployeeUsername.getRichStringCellValue().getString().trim();
//					} else if (cellemployeeUsername.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//						DataFormatter formatter = new DataFormatter();
//						employeeUsername = formatter.formatCellValue(cellemployeeUsername).trim();
//					}
//				}

				if (employeeAssociateId == null || employeeAssociateId.isEmpty()) {
					listErrorMessage.add( " AssociateId is empty in row  " + index );

				} else {
					String s = employeeAssociateId;
					long employeeAssociateId1 = Long.parseLong(s);

					User employeeAssociateIdCheck = userService.getByAssociateId(employeeAssociateId);
					if (employeeAssociateIdCheck != null) {
						listErrorMessage.add(employeeAssociateId + " in row " + index + " already exists. ");
					}
				}

				if (employeeFirstname == null || employeeFirstname.isEmpty()) {
//					error = true;
					listErrorMessage.add( "First Name is empty in row  " + index);
				}

//				if (employeeLastname == null || employeeLastname.isEmpty()) {
//						listErrorMessage.add( "Last Name is empty in row  " + index );
//				}
				if (employeeDateOfBirth == null || employeeDateOfBirth.isEmpty()) {
					
					listErrorMessage.add("Date Of Birth is empty in row  " + index);

				} 
				if (employeeEmail == null || employeeEmail.isEmpty()) {
//					error = true;
//					listErrorMessage.add( employeeAssociateId +" in row " + index + " already exists. ");
					listErrorMessage.add( "Email is empty in row  " + index );

				} else {
//					String s = employeeAssociateId;
//					long employeeAssociateId1 = Long.parseLong(s);

					User employeeEmailCheck = userService.getByEmail(employeeEmail);
					if (employeeEmailCheck != null) {
						listErrorMessage.add(employeeEmail + " in row " + index + " already exists. ");
					}
				}

				if (employeePhoneNumber == null || employeePhoneNumber.isEmpty()) {
//					error = true;
//						listErrorMessage.add( hierarchyHeader.getLevel1Name()+" is empty in row  " + index );
					listErrorMessage.add("Phone Number is empty in row  " + index );

				}

				if (employeeUAN == null || employeeUAN.isEmpty()) {
//					error = true;
					listErrorMessage.add("UAN is empty in row  " + index );
				} else {
					Optional<User> employeeUANCheck = userService.findByUAN(employeeUAN);
					if (employeeUANCheck.isPresent()) {
//						error = true;
						listErrorMessage.add(employeeUAN + " in row " + index + " already exists. ");
					}
				}

				if (employeeDesignation == null || employeeDesignation.isEmpty()) {
//					error = true;
					listErrorMessage.add( "Designation is empty in row  " + index );
				}
				if (employeeGender == null || employeeGender.isEmpty()) {
//					error = true;
					listErrorMessage.add( "Gender is empty in row  " + index );
				}

				if (employeeUnit == null || employeeUnit.isEmpty()) {
//					error = true;
					listErrorMessage.add( "Unit is empty in row  " + index );
				}
				if (employeeDOJ == null || employeeDOJ.isEmpty()) {
//					error = true;
					listErrorMessage.add("DOJ is empty in row  " + index );
				}
				if (employeeBankName == null || employeeBankName.isEmpty()) {
//					error = true;
					listErrorMessage.add( "Bank Name is empty in row  " + index );
				}
				if (employeeBankAccountNumber == null || employeeBankAccountNumber.isEmpty()) {
//					error = true;
					listErrorMessage.add("Bank Account Number is empty in row  " + index );
				} else {
					Optional<User> employeeBankAccountNumberCheck = userService
							.findByBankAccountNumber(employeeBankAccountNumber);
					if (employeeBankAccountNumberCheck.isPresent()) {
//						error = true;
						listErrorMessage.add(employeeBankAccountNumber + " in row " + index + " already exists. ");
					}
				}

//				if (employeeUsername == null || employeeUsername.isEmpty()) {
//					error = true;
////						listErrorMessage.add( hierarchyHeader.getLevel1Name()+" is empty in row  " + index );
//				} else {
//					Optional<User> employeeUsernameCheck = userService.findByUsername(employeeUsername);
//					if (employeeUsernameCheck.isPresent()) {
////						error = true;
//						listErrorMessage.add(employeeUsername + " in row " + index + " already exists. ");
//					}
//				}
				if (employeePassword == null || employeePassword.isEmpty()) {
					listErrorMessage.add( "Password is empty in row  " + index );
//					listErrorMessage.add("PASSWORD IS NULL" + index);
				}

				if (listErrorMessage.isEmpty()) {

					Long userRole = (long) 2;

					Optional<Role> role = roleService.getById(userRole);

					String assIdValue = employeeAssociateId;
					long assciateIdvalue = Long.parseLong(assIdValue);

					user.setId(null);
					user.setAssociateId(employeeAssociateId);
					user.setFirstname(employeeFirstname);
//					user.setLastname(employeeLastname);
					user.setDateOfBirth(employeeDateOfBirth);
					user.setEmail(employeeEmail);
					user.setPhoneNumber(employeePhoneNumber);
					user.setUsername(employeeAssociateId);
					user.setPassword(bcryptPasswordEncoder.encode(employeePassword));
					user.setPermissions("ADD,EDIT,DELETE,LIST");
					user.setRole(role.get());
					user.setStatus(true);
					user.setBankAccountNumber(employeeBankAccountNumber);
					user.setBankName(employeeBankName);
					user.setDesignation(employeeDesignation);
					user.setDoj(employeeDOJ);
					user.setGender(employeeGender);
					user.setUan(employeeUAN);
					user.setUnit(employeeUnit);
					user.setProfilePath("");
					

				} else {
//					listErrorMessage.add("Empty row");
				}

			}

//			employeeBulkUpload.setError(error);
//			String assIdValue = employeeAssociateId;
//			long assciateIdvalue = Long.parseLong(assIdValue);
//			employeeBulkUpload.setAssociateId(assciateIdvalue);
//			employeeBulkUpload.setUser(employee);
//			employeeBulkUpload.setAssociateId(null);
//			employeeBulkUpload.setFirstname(employeeFirstname);
//			employeeBulkUpload.setLastname(employeeLastname);
//			employeeBulkUpload.setEmail(employeeEmail);
//			employeeBulkUpload.setPhoneNumber(employeePhoneNumber);
//			employeeBulkUpload.setUsername(employeeUsername);
//			employeeBulkUpload.setPassword(bcryptPasswordEncoder.encode(employeePassword));
			employeeBulkUpload.setErrorMessage(listErrorMessage);
			employeeBulkUpload.setUser(user);
			return employeeBulkUpload;

		}
		return null;
	}

	private boolean validateEmployeeDataUpload(Row row) {
		boolean validateTemplateFlag = true;
		if (row == null) {
			return validateTemplateFlag = false;
		} else {
			try {
				String associateId = row.getCell(0).getRichStringCellValue().getString();
				String firstname = row.getCell(1).getRichStringCellValue().getString().trim();
				String dateOfBirth = row.getCell(2).getRichStringCellValue().getString().trim();
				String  email= row.getCell(3).getRichStringCellValue().getString().trim();
				String  password= row.getCell(4).getRichStringCellValue().getString().trim();
				String  phoneNumber= row.getCell(5).getRichStringCellValue().getString().trim();
				String  uan= row.getCell(6).getRichStringCellValue().getString().trim();
				String  designation= row.getCell(7).getRichStringCellValue().getString().trim();
				String  gender= row.getCell(8).getRichStringCellValue().getString().trim();
				String  unit= row.getCell(9).getRichStringCellValue().getString().trim();
				String  dateOfJoining= row.getCell(10).getRichStringCellValue().getString().trim();
				String  bankName= row.getCell(11).getRichStringCellValue().getString().trim();

				String  bankAccountNumber= row.getCell(12).getRichStringCellValue().getString().trim();

				if (!(associateId.trim()).equals("Associate Id" + "*")) {
					return validateTemplateFlag = false;
				}
				if (!(firstname).equals("Associate Name")) {
					return validateTemplateFlag = false;
				}
//				if (!(lastname).equals("Last Name")) {
//					return validateTemplateFlag = false;
//				}
				if (!(dateOfBirth).equals("Date Of Birth")) {
					return validateTemplateFlag = false;
				}
				if (!(email.trim()).equals("Email"  + "*")) {
					return validateTemplateFlag = false;
				}
				if (!(password).equals("Password")) {
					return validateTemplateFlag = false;
				}
				if (!(phoneNumber).equals("Phone Number")) {
					return validateTemplateFlag = false;
				}
				if (!(uan).equals("UAN"  + "*")) {
					return validateTemplateFlag = false;
				}
				if (!(designation).equals("Designation")) {
					return validateTemplateFlag = false;
				}
				if (!(gender).equals("Gender")) {
					return validateTemplateFlag = false;
				}
				if (!(unit).equals("Unit")) {
					return validateTemplateFlag = false;
				}
				if (!(dateOfJoining).equals("Date Of Joining")) {
					return validateTemplateFlag = false;
				}
				if (!(bankName).equals("Bank Name"  + "*")) {
					return validateTemplateFlag = false;
				}
				if (!(bankAccountNumber).equals("Bank Account Number"  + "*")) {
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

//	private ResponseEntity<Object> responseCreation(boolean status, String message, String description,
//			 {
//		// TODO Auto-generated method stub
//		response.setStatus(status);
//		response.setInformation(new ExceptionBean(new Date(), message, description));
//		if (string != null) {
//			response.setMessage(string);
//			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "message" });
//		} else {
//			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
//		}
//		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
//	}

}
