/**
 * 
 */
package com.pay.payslip.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;
import com.pay.payslip.model.User;
import com.pay.payslip.repository.CustomRepository;
import com.pay.payslip.servie.MonthAndYearService;
import com.pay.payslip.servie.MonthService;
import com.pay.payslip.servie.PaySlipService;
import com.pay.payslip.servie.RoleService;
import com.pay.payslip.servie.UserService;
import com.pay.payslip.util.FilterUtil;
import com.pay.payslip.util.ResponseModel;

//import org.apache.log4j.Logger;
/**
 * @author Leo Navin
 *
 */
@RestController
public class DynamicPdfGenerate {

	final static Logger logger = LoggerFactory.getLogger(DynamicPdfGenerate.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private MonthService monthService;

	@Autowired
	private CustomRepository customRepository;

	private NumberToWordsConverter numberToWordsConverter;

	@Autowired
	private MonthAndYearService monthAndYearService;

	@Autowired
	private PaySlipService paySlipService;

	private ResponseModel response = new ResponseModel();

	private MappingJacksonValue mappingJacksonValue;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

//	public static void main(String[] args) throws DocumentException, IOException {
	// reader for the src file
//        Pdf reader = new PdfReader("C:/Users/BoscoSoft/Downloads/slip11.pdf");
	// initializations
//        Document document = new Document(PageSize.A4, 50, 50, 50, 50);  
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/BoscoSoft/Downloads/slip11.pdf"));  
//                   
//                document.open();  
//              
//                  Paragraph paragraph1 = new Paragraph();  
//               
//                  paragraph1.setSpacingBefore(100);  
//               
//              
//                  document.add(paragraph1);  
//               
//            document.add(new Paragraph("Somejghj more text on the first page with different color and font type.",   
//               
//            FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new CMYKColor(0, 255, 0, 0))));  
//             document.close();   

	@GetMapping("/exportpdf")
	private void createPayslipPDFReport(@RequestHeader("Authorization") String token,
			@RequestParam("userId") Long userId, @RequestParam("pagecount") Long pagecount,
			HttpServletResponse response) throws IOException, ParseException {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		try {
			logger.info("createPayslipPDFReport inside the method");

//			String headerValue="attachment;filename=pdf_"+currentDateTime+".pdf";

			StringBuilder dispatchScheduleQuery = new StringBuilder();

//			dispatchScheduleQuery.append("SELECT monthAndYear FROM MonthAndYear  WHERE monthAndYear ORDER BY monthYear.id DESC LIMIT  "
//					+pagecount);

			dispatchScheduleQuery.append("SELECT p FROM MonthAndYear p ORDER BY p.id DESC");

			int page = pagecount.intValue();

			List<MonthAndYear> monthAndYearList = customRepository.getMonthAndYearList(dispatchScheduleQuery.toString(),
					page);

//			System.out.println("buyerProductList" + monthAndYearList);

//			for (MonthAndYear id2 : monthAndYearList) {

			logger.info("Here get in MonthAndYear id " + monthAndYearList);

			Optional<User> userAssociateId = (logInUser.get().getRole().getRole().equals("ADMIN"))
					? userId != null ? userService.findById(userId) : null
					: null;

			logger.info("Here get Admin Associate Id " + userAssociateId);

			String id = (logInUser.get().getRole().getRole().equals("ADMIN")) ? userAssociateId.get().getAssociateId()
					: logInUser.get().getAssociateId();

			Optional<User> user = userService.findByAssociateId(id);

			if (user.isPresent()) {

				Date date = new Date();

				response.setContentType("application/pdf");
//			String fileName = "Boscosoft PaySlip "+date.getTime() + ".pdf";

				String fileName = "Boscosoft_PaySlip_" + user.get().getFirstname().replaceAll("\\s+", "_") + "_" + date.getTime() + ".pdf";
				String headerKey = "Content-Disposition";
				String headerValue = "attachment;filename=\"" + fileName + "\"";

				String pragma = "Pragma";
				String pragmaValue = fileName.replaceAll("\\s+", "_");

				response.setHeader(headerKey, headerValue);

				response.setHeader(pragma, pragmaValue);

//			}

//			String contextPath = ConstantUtil.CONTEXT_PATH;

//			FileOutputStream fileOutputStream = null;
//			File file = null;
//			try {
//				file = new File(contextPath + fileName);
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//				fileOutputStream = new FileOutputStream(file);
//			} catch (FileNotFoundException ex) {
//				ex.printStackTrace();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}

//			if (!new File(ConstantUtil.CONTEXT_PATH).exists()) {
//				new File(ConstantUtil.CONTEXT_PATH).mkdir();
//			}

				ArrayList<PaySlip> payslipDataList = new ArrayList<PaySlip>();

				for (MonthAndYear id2 : monthAndYearList) {

					String id1 = (logInUser.get().getRole().getRole().equals("ADMIN"))
							? userAssociateId.get().getAssociateId()
							: logInUser.get().getAssociateId();

					PaySlip payslip = paySlipService.findByAssociateIdAndMonthId(id1, id2.getId());

					if (payslip != null) {
						payslipDataList.add(payslip);
					} else {

					}

				}

				int size = payslipDataList.size();

//			for (MonthAndYear id2 : monthAndYearList) {
//
//				long id1 = (logInUser.get().getRole().getRole().equals("ADMIN")) ? 2022001
//						: logInUser.get().getAssociateId();
//
//				PaySlip payslip = paySlipService.findByAssociateIdAndMonthId(id1, id2.getId());

				if (payslipDataList.size() > 0) {

					Document document = new Document(PageSize.A4);
					document.setMargins(10, 10, 10, 114);
					document.setMarginMirroring(false);

//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(contextPath + fileName));
//			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

					PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
					Rectangle rect = new Rectangle(400, 600, 50, 45);
					writer.setBoxSize("commonBorder", rect);
					EventHandler pageEventHelper = new EventHandler();
					writer.setPageEvent(pageEventHelper);
					document.open();

					for (PaySlip payslip : payslipDataList) {

						logger.info("Inside the pdf data printed " + payslip.getUser().getAssociateId() + " "
								+ payslip.getMonthAndYear().getMonth().getMonth());

						if (payslip != null && payslip.getUser().getAssociateId() != null) {
//			int size = monthAndYearList.size();
//
//			for (MonthAndYear id2 : monthAndYearList) {
//
//				long id1 = (logInUser.get().getRole().getRole().equals("ADMIN")) ? 2022001
//						: logInUser.get().getAssociateId();
//
//				PaySlip payslip = paySlipService.findByAssociateIdAndMonthId(id1, id2.getId());
//
//				if (payslip != null && payslip.getUser().getAssociateId() != null) {

							float fontSize = 10;
							float fontSize1 = 9;
							float[] tableColumns = { 400 };
							float[] tableColumns1 = { 100, 300 };
							float[] tableColumns2 = { 10, 390 };
							float[] tableColumns3 = { 20, 380 };
							float[] tableColumns4 = { 50, 200, 50, 50, 50 };
							float[] tableColumns5 = { 75, 75, 75, 100, 75 };
							float[] tableColumns6 = { 200, 200 };
							float[] tableColumns7 = { 100, 100, 100, 100 };

							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

							PdfPTable table = null;
							float[] table5Columns = { 300, 300 };
//				table = new PdfPTable(table5Columns);
//				table.setWidthPercentage(86);

							PdfPCell tableCell = null;

							Phrase content = null;
							Paragraph paragraph = null;

//				long employeeID = payslip.getId();

							/* Basic Pay */

//					System.out.println(((Object) payslip.getBasic().getClass().getSimpleName()));

							double basic0 = Double.parseDouble(payslip.getBasic().replace(",", ""));
							long basic1 = (new Double(basic0)).longValue(); // 129
//				long basic = (long) basic1; //129
							String basic = String.valueOf(basic1);

							/* Loss of Pay */
							double lossOfPay0 = Double.parseDouble(payslip.getLop().replace(",", ""));
							long lossOfPay1 = (new Double(lossOfPay0)).longValue(); // 129
							String lossOfPay = String.valueOf(lossOfPay1);

//				long lossOfPay = (long) lossOfPay1; //129

							/* House Allowance */
							double hRA0 = Double.parseDouble(payslip.gethRA().replace(",", ""));
							long hRA1 = (new Double(hRA0)).longValue(); // 129
							String hRA = String.valueOf(hRA1);
//				long hRA = (long) hRA1; //129

							/* Employee PF Contribution */
							double pf0 = Double.parseDouble(payslip.getPf().replace(",", ""));
							long pf1 = (new Double(pf0)).longValue(); // 129
							String pf = String.valueOf(pf1);

//				long pf = (long) pf1; //129

							/* Leave Travel Allowance */
							double lta0 = Double.parseDouble(payslip.getlTA().replace(",", ""));
							long lta1 = (new Double(lta0)).longValue(); // 129
							String lta = String.valueOf(lta1);
//				long lta = (long) lta1; //129

							/* TDS */
							double tds0 = Double.parseDouble(payslip.gettDS().replace(",", ""));
							long tds1 = (new Double(tds0)).longValue(); // 129
							String tds = String.valueOf(tds1);

//				long tds = (long) tds1; //129

							/* Medical Allowance */
							double medicalAllownce0 = Double.parseDouble(payslip.getMedicalAll().replace(",", ""));
							long medicalAllownce1 = (new Double(medicalAllownce0)).longValue(); // 129
							String medicalAllownce = String.valueOf(medicalAllownce1);

//				long medicalAllownce = (long) medicalAllownce1; //129

							/* Mess Payment */
							double messPay0 = Double.parseDouble(payslip.getMess().replace(",", ""));
							long messPay1 = (new Double(messPay0)).longValue(); // 129
							String messPay = String.valueOf(messPay1);

//				long messPay = (long) messPay1; //129

							/* Conveyance Allowance */
							double conveyanceAllownce0 = Double.parseDouble(payslip.getConveyance().replace(",", ""));
							long conveyanceAllownce1 = (new Double(conveyanceAllownce0)).longValue(); // 129
							String conveyanceAllownce = String.valueOf(conveyanceAllownce1);

//				long conveyanceAllownce = (long) conveyanceAllownce1; //129

							/* Hostel Payment */
							double hostelPay0 = Double.parseDouble(payslip.getHostel().replace(",", ""));
							long hostelPay1 = (new Double(hostelPay0)).longValue(); // 129
							String hostelPay = String.valueOf(hostelPay1);

//				long hostelPay = (long) hostelPay1; //129

							/* Special Pay */
							double specialPay0 = Double.parseDouble(payslip.getSplAllow().replace(",", ""));
							long specialPay1 = (new Double(specialPay0)).longValue(); // 129
							String specialPay = String.valueOf(specialPay1);

//				long specialPay = (long) specialPay1; //129

							/* Staff Benefit Fund */
							double staffBenefitFund0 = Double.parseDouble(payslip.getsBF().replace(",", ""));
							long staffBenefitFund1 = (new Double(staffBenefitFund0)).longValue(); // 129
							String staffBenefitFund = String.valueOf(staffBenefitFund1);

//				long staffBenefitFund = (long) staffBenefitFund1; //129

							/* Advance/Loan Repayment */
							double advanceLoan0 = Double.parseDouble(payslip.getAdvanceLoan().replace(",", ""));
							long advanceLoan1 = (new Double(advanceLoan0)).longValue(); // 129
							String advanceLoan = String.valueOf(advanceLoan1);

//				long advanceLoan = (long) advanceLoan1; //129

							/* Insurance */
							double insurance0 = Double.parseDouble(payslip.getInsurance().replace(",", ""));
							long insurance1 = (new Double(insurance0)).longValue(); // 129
							String insurance = String.valueOf(insurance1);

//				long insurance = (long) insurance1; //129

							/* Gross Pay (Rounded) error coming for 6,000 (, is get an exception) */
//				double grossPay0 = 0;
//				try {
//					grossPay0 = DecimalFormat.getNumberInstance().parse(payslip.getGross()).doubleValue();
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				long grossPay1 = (new Double(grossPay0)).longValue(); //129
//				String grossPay=String.valueOf(grossPay1);

							double grossEarning0 = Double.parseDouble(payslip.getGrossEarning().replace(",", ""));
							long grossEarning1 = (new Double(grossEarning0)).longValue(); // 129
							String grossEarning = String.valueOf(grossEarning1);

							/* Total Deductions (Rounded) */
							double totalDeduction0 = Double.parseDouble(payslip.getTotalDeduction().replace(",", ""));
							long totalDeduction1 = (new Double(totalDeduction0)).longValue(); // 129
							String totalDeduction = String.valueOf(totalDeduction1);

//				long totalDeduction = (long) totalDeduction1; //129

							/* Net Pay */
							double netPay0 = Double.parseDouble(payslip.getNetPay().replace(",", ""));
							long netPay1 = (new Double(netPay0)).longValue(); // 129
							String netPay = String.valueOf(netPay1);

//				long netPay = (long) netPay1; //129

//				long employeePFContribution = Long.parseLong(payslip.getPfEmployerContribution());
//				long houseAllownce = Long.parseLong(payslip.getCityAllowance());

							// first 1 column

							float[] table1Columns = { 140, 140, 120, 160 };
							table = new PdfPTable(table1Columns);
							table.setWidthPercentage(80);

//				document.add(Chunk.NEWLINE);
//				document.add(Chunk.NEWLINE);
//				document.add(Chunk.NEWLINE);
//				document.add(Chunk.NEWLINE);

							content = new Phrase("Employee ID", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getUser().getAssociateId().toString(),
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase("Month ", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getMonthAndYear().getMonth().getMonth(),
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);
//					document.add(new Paragraph("\r"));
							document.add(table);

							// 2 column

							table = new PdfPTable(table1Columns);
							table.setWidthPercentage(80);

							content = new Phrase("Employee Name", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(
									payslip.getUser().getFirstname(),
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase("Year", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getMonthAndYear().getYear(),
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);
							document.add(table);
//				document.add(new Paragraph("\r"));

							// 3 column
							table = new PdfPTable(table1Columns);
							table.setWidthPercentage(80);
							content = new Phrase("Designation", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getUser().getDesignation(),
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase("DOJ ", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							// Here Date Formate Change in YYYY-MM-DD to dd-mm-yyyy;
							String ds1 = payslip.getUser().getDoj();
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
							String ds2 = sdf2.format(sdf1.parse(ds1));

							content = new Phrase(ds2, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);
							document.add(table);
//				document.add(new Paragraph("\r"));

							// 4 column

							table = new PdfPTable(table1Columns);
							table.setWidthPercentage(80);

							content = new Phrase("Unit", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getUser().getUnit(), new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase("UAN", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getUser().getUan(), new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);
							document.add(table);
//				document.add(new Paragraph("\r"));

							// 5 column

							table = new PdfPTable(table1Columns);
							table.setWidthPercentage(80);

							content = new Phrase("Days Worked", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getDaysWorked(), new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase("Bank A/C", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);

							content = new Phrase(payslip.getUser().getBankAccountNumber(),
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							table.addCell(tableCell);
							document.add(table);

							float[] tableColumns44 = { 180, 100, 180, 100 };
							table = new PdfPTable(tableColumns44);
							table.setWidthPercentage(80);

							content = new Phrase("EMOLUMENTS", new Font(FontFamily.HELVETICA, 9, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);
//				tableCell.addElement(content);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							tableCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
							table.addCell(tableCell);

							// table header here
							content = new Phrase("AMOUNT Rs.", new Font(FontFamily.HELVETICA, 9, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							tableCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
							table.addCell(tableCell);

							content = new Phrase("DEDUCTIONS", new Font(FontFamily.HELVETICA, 9, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							tableCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
							table.addCell(tableCell);

							content = new Phrase("AMOUNT Rs.", new Font(FontFamily.HELVETICA, 9, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							tableCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//				document.add(new Paragraph("\r"));
//					document.add(new Paragraph("\r"));
							document.add(new Paragraph("\r"));
							table.addCell(tableCell);

//				BigDecimal bd = new BigDecimal("1.23456789");
//				System.out.println(bd.setScale(0,BigDecimal.ROUND_HALF_UP));

							// table body 1 content here
							content = new Phrase("Basic Pay", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

//				content = new Phrase(payslip.getBasic(), new Font(FontFamily.HELVETICA, fontSize));
							content = new Phrase(basic, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Loss of Pay", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

//				content = new Phrase(payslip.getLop(), new Font(FontFamily.HELVETICA, fontSize));
							content = new Phrase(lossOfPay, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 2 Content
							content = new Phrase("House Allowance", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(hRA, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Employee PF Contribution", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

//				content = new Phrase(payslip.getPfEmployerContribution(), new Font(FontFamily.HELVETICA, fontSize));
							content = new Phrase(pf, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 3 content
							content = new Phrase("Leave Travel Allowance", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(lta, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("TDS", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(tds, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 4 content
							content = new Phrase("Medical Allowance ", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(medicalAllownce, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Mess Payment", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(messPay, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 5 content
							content = new Phrase("Conveyance Allowance", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(conveyanceAllownce, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Hostel Payment", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(hostelPay, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);

							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 6 content
							content = new Phrase("Special Pay", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(specialPay, new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Staff Benefit Fund", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(staffBenefitFund, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 7 content
							content = new Phrase("", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Advance/Loan Repayment", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(advanceLoan, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 8 content
							content = new Phrase("", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Insurance", new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							tableCell.addElement(content);
							tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase(insurance, new Font(FontFamily.HELVETICA, fontSize));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 9 content
							content = new Phrase("Gross Pay (Rounded)",
									new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);

							// tableCell.setBorder(Rectangle.LEFT);
//				tableCell.setBorder(Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

//				content = new Phrase(payslip.getGrossEarning(), new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							content = new Phrase(grossEarning, new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							// tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Total Deductions (Rounded)",
									new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);
							// tableCell.setBorder(Rectangle.LEFT);

							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

//				content = new Phrase(payslip.getTotalDeduction(),new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							content = new Phrase(totalDeduction, new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
							// tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							// table 10 content

							content = new Phrase("Net Salary", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_CENTER);
							tableCell.addElement(paragraph);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							tableCell.setColspan(3);
							table.addCell(tableCell);

//				content = new Phrase(payslip.getNetPay(), new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							content = new Phrase(netPay, new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							paragraph.add(content);
							tableCell.addElement(paragraph);
//				tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
							tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);
							document.add(table);

							float[] onecol = { 50, 350 };
							table = new PdfPTable(onecol);
							table.setWidthPercentage(80);
//				content = new Phrase("", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
//				tableCell = new PdfPCell();
//				paragraph = new Paragraph();
//				paragraph.add(content);
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
//				tableCell.addElement(paragraph);
//				tableCell.setBorder(Rectangle.NO_BORDER);
//				tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(tableCell);

							content = new Phrase("In Words :-",
									new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_LEFT);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

//						content = new Phrase(" ", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
//						tableCell = new PdfPCell();
//						paragraph = new Paragraph();
//						paragraph.add(content);
//						paragraph.setAlignment(Element.ALIGN_RIGHT);
//						tableCell.addElement(paragraph);
//						tableCell.setBorder(Rectangle.NO_BORDER);
//						tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//						table.addCell(tableCell);

							int rupees = (int) netPay1;
							String rupeesInWordConvert = numberToWordsConverter.convert(rupees);
							content = new Phrase(rupeesInWordConvert + " Only.",
									new Font(FontFamily.HELVETICA, fontSize));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_LEFT);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);
							table.setSpacingBefore(10);
							table.setSpacingAfter(5);
//						document.add(new Paragraph("\r"));
//						document.add(new Paragraph("\r"));
							document.add(table);
							document.add(new Paragraph("\r"));
							document.add(new Paragraph("\r"));
//							document.add(new Paragraph("\r"));
//							document.add(new Paragraph("\r"));
//						document.add(new Paragraph("\r"));

//						document.add(new Paragraph("\r"));

							float[] twocol = { 200, 200 };
							table = new PdfPTable(twocol);
							table.setWidthPercentage(80);
//				content = new Phrase("", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
//				tableCell = new PdfPCell();
//				paragraph = new Paragraph();
//				paragraph.add(content);
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
//				tableCell.addElement(paragraph);
//				tableCell.setBorder(Rectangle.NO_BORDER);
//				tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(tableCell);

							content = new Phrase("---------------------------",
									new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
						paragraph.setAlignment(Element.ALIGN_LEFT);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
//							tableCell.setPaddingLeft(6);
//						tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("----------------------------",
									new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
//							tableCell.setPaddingLeft(25);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);
//						table.setSpacingBefore(10);
//						table.setSpacingAfter(5);
//						document.add(new Paragraph("\r"));
//						document.add(new Paragraph("\r"));
//							document.add(table);

							content = new Phrase("Employer Signature",
									new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));

							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_LEFT);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
//							tableCell.setPaddingLeft(6);
//						tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);

							content = new Phrase("Employee Signature", new Font(FontFamily.HELVETICA, fontSize, Font.BOLD));
							tableCell = new PdfPCell();
							paragraph = new Paragraph();
							paragraph.add(content);
							paragraph.setAlignment(Element.ALIGN_RIGHT);
							tableCell.addElement(paragraph);
							tableCell.setBorder(Rectangle.NO_BORDER);
//							tableCell.setPaddingLeft(25);
							tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(tableCell);
//						table.setSpacingBefore(10);
//						table.setSpacingAfter(5);
//						document.add(new Paragraph("\r"));
//						document.add(new Paragraph("\r"));
							document.add(table);

							size = size - 1;
							if (size != 0) {
								logger.info("Inside the pdf new page created " + size);

								document.newPage();
							} else {
								logger.info("Not pdf new page created " + size);
							}
						} else {
							size = size - 1;
						}

					}
					document.close();

				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
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
