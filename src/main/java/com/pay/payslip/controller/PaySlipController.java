/**
 * 
 */
package com.pay.payslip.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
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
@RequestMapping(value = "/payslip")
public class PaySlipController {

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

	@GetMapping("/getAllPaySlip")
	public ResponseEntity<Object> getAllPaySlip(@RequestHeader("Authorization") String token,
			@RequestParam("id") Long id) throws ParseException {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			List<PaySlip> payslipList = new ArrayList<>();

			List<PaySlip> paysliplist = paySlipService.findByAllPaySlip(id);

			System.out.println(paysliplist.size());

			boolean deleteStatus = true;
			if (paysliplist.size() > 0) {

				for (PaySlip payslip : paysliplist) {

					/* Basic Pay */
//			double basic0 = Double.parseDouble(payslip.getBasic());
					double basic0 = DecimalFormat.getNumberInstance().parse(payslip.getBasic()).doubleValue();
					long basic1 = (new Double(basic0)).longValue(); // 129
					String basic = String.valueOf(basic1);

					/* Loss of Pay */
//			double lossOfPay0 = Double.parseDouble(payslip.getLop());
					double lossOfPay0 = DecimalFormat.getNumberInstance().parse(payslip.getLop()).doubleValue();
					long lossOfPay1 = (new Double(lossOfPay0)).longValue(); // 129
					String lossOfPay = String.valueOf(lossOfPay1);

					/* House Allowance */
//			double hRA0 = Double.parseDouble(payslip.gethRA());
					double hRA0 = DecimalFormat.getNumberInstance().parse(payslip.gethRA()).doubleValue();
					long hRA1 = (new Double(hRA0)).longValue(); // 129
					String hRA = String.valueOf(hRA1);

					/* Employee PF Contribution */
//			double pf0 = Double.parseDouble(payslip.getPf());
					double pf0 = DecimalFormat.getNumberInstance().parse(payslip.getPf()).doubleValue();
					long pf1 = (new Double(pf0)).longValue(); // 129
					String pf = String.valueOf(pf1);

					/* Leave Travel Allowance */
//			double lta0 = Double.parseDouble(payslip.getlTA());
					double lta0 = DecimalFormat.getNumberInstance().parse(payslip.getlTA()).doubleValue();
					long lta1 = (new Double(lta0)).longValue(); // 129
					String lta = String.valueOf(lta1);

					/* TDS */
//			double tds0 = Double.parseDouble(payslip.gettDS());
					double tds0 = DecimalFormat.getNumberInstance().parse(payslip.gettDS()).doubleValue();
					long tds1 = (new Double(tds0)).longValue(); // 129
					String tds = String.valueOf(tds1);

					/* Medical Allowance */
//			double medicalAllownce0 = Double.parseDouble(payslip.getMedicalAll());
					double medicalAllownce0 = DecimalFormat.getNumberInstance().parse(payslip.getMedicalAll())
							.doubleValue();
					long medicalAllownce1 = (new Double(medicalAllownce0)).longValue(); // 129
					String medicalAllownce = String.valueOf(medicalAllownce1);

					/* Mess Payment */
//			double messPay0 = Double.parseDouble(payslip.getMess());
					double messPay0 = DecimalFormat.getNumberInstance().parse(payslip.getMess()).doubleValue();
					long messPay1 = (new Double(messPay0)).longValue(); // 129
					String messPay = String.valueOf(messPay1);

					/* Conveyance Allowance */
//			double conveyanceAllownce0 = Double.parseDouble(payslip.getConveyance());
					double conveyanceAllownce0 = DecimalFormat.getNumberInstance().parse(payslip.getConveyance())
							.doubleValue();
					long conveyanceAllownce1 = (new Double(conveyanceAllownce0)).longValue(); // 129
					String conveyanceAllownce = String.valueOf(conveyanceAllownce1);

					/* Hostel Payment */
//			double hostelPay0 = Double.parseDouble(payslip.getHostel());
					double hostelPay0 = DecimalFormat.getNumberInstance().parse(payslip.getHostel()).doubleValue();
					long hostelPay1 = (new Double(hostelPay0)).longValue(); // 129
					String hostelPay = String.valueOf(hostelPay1);

					/* Special Pay */
//			double specialPay0 = Double.parseDouble(payslip.getSplAllow());
					double specialPay0 = DecimalFormat.getNumberInstance().parse(payslip.getSplAllow()).doubleValue();
					long specialPay1 = (new Double(specialPay0)).longValue(); // 129
					String specialPay = String.valueOf(specialPay1);

					/* Staff Benefit Fund */
//			double staffBenefitFund0 = Double.parseDouble(payslip.getsBF());
					double staffBenefitFund0 = DecimalFormat.getNumberInstance().parse(payslip.getsBF()).doubleValue();
					long staffBenefitFund1 = (new Double(staffBenefitFund0)).longValue(); // 129
					String staffBenefitFund = String.valueOf(staffBenefitFund1);

					/* Advance/Loan Repayment */
//			double advanceLoan0 = Double.parseDouble(payslip.getAdvanceLoan());
					double advanceLoan0 = DecimalFormat.getNumberInstance().parse(payslip.getAdvanceLoan())
							.doubleValue();
					long advanceLoan1 = (new Double(advanceLoan0)).longValue(); // 129
					String advanceLoan = String.valueOf(advanceLoan1);

					/* Insurance */
//			double insurance0 = Double.parseDouble(payslip.getInsurance());
					double insurance0 = DecimalFormat.getNumberInstance().parse(payslip.getInsurance()).doubleValue();
					long insurance1 = (new Double(insurance0)).longValue(); // 129
					String insurance = String.valueOf(insurance1);

					/* Gross Pay (Rounded) error coming for 6,000 (, is get an exception) */
//			double grossPay0 = DecimalFormat.getNumberInstance().parse(payslip.getGross()).doubleValue();
//			long grossPay1 = (new Double(grossPay0)).longValue(); //129
//			String grossPay=String.valueOf(grossPay1);

//			double grossEarning0 = Double.parseDouble(payslip.getGrossEarning());
					double grossEarning0 = DecimalFormat.getNumberInstance().parse(payslip.getGrossEarning())
							.doubleValue();
					long grossEarning1 = (new Double(grossEarning0)).longValue(); // 129
					String grossEarning = String.valueOf(grossEarning1);

					/* Total Deductions (Rounded) */
//			double totalDeduction0 = Double.parseDouble(payslip.getTotalDeduction());
					double totalDeduction0 = DecimalFormat.getNumberInstance().parse(payslip.getTotalDeduction())
							.doubleValue();
					long totalDeduction1 = (new Double(totalDeduction0)).longValue(); // 129
					String totalDeduction = String.valueOf(totalDeduction1);

//			long totalDeduction = (long) totalDeduction1; //129
					/* Net Pay */
//			double netPay0 = Double.parseDouble(payslip.getNetPay());
					double netPay0 = DecimalFormat.getNumberInstance().parse(payslip.getNetPay()).doubleValue();
					long netPay1 = (new Double(netPay0)).longValue(); // 129
					String netPay = String.valueOf(netPay1);

					/* CTC */
//			double cTC0 = DecimalFormat.getNumberInstance().parse(payslip.getcTC()).doubleValue();
//			long cTC1 = (new Double(cTC0)).longValue(); //129
//			String cTC=String.valueOf(cTC1);

					payslip.setBasic(basic);
					payslip.setLop(lossOfPay);
					payslip.sethRA(hRA);
					payslip.setPf(pf);
					payslip.setlTA(lta);
					payslip.settDS(tds);
					payslip.setMedicalAll(medicalAllownce);
					payslip.setMess(messPay);
					payslip.setConveyance(conveyanceAllownce);
					payslip.setHostel(hostelPay);
					payslip.setSplAllow(specialPay);
					payslip.setsBF(staffBenefitFund);
					payslip.setAdvanceLoan(advanceLoan);
					payslip.setInsurance(insurance);
//			payslip.setGross(grossPay);
					payslip.setGrossEarning(grossEarning);
					payslip.setTotalDeduction(totalDeduction);
					payslip.setNetPay(netPay);
//			payslip.setcTC(cTC);
					if (payslip.getUser().getAssociateId() != null) {
						deleteStatus = false;
					} else {
						deleteStatus = true;
					}
					payslip.setDeleteStatus(true);

					payslipList.add(payslip);

				}

				return responseCreation(true, "Success", "Get All User data", payslipList, null);

			}
			return responseCreation(false, "Failed", "Data is Null", null, null);

		} else {
			if (logInUser.isPresent()) {

				PaySlip payslip = paySlipService.findByAssociateIdAndMonthId(logInUser.get().getAssociateId(), id);

				if (payslip != null) {
					if (payslip.getUser().getId().equals(logInUser.get().getId())) {

//					List<PaySlip> payslip1 = new ArrayList<PaySlip>();
//
//					payslip1.add(payslip);

						double basic0 = DecimalFormat.getNumberInstance().parse(payslip.getBasic()).doubleValue();
						long basic1 = (new Double(basic0)).longValue(); // 129
						String basic = String.valueOf(basic1);
						
						/* Loss of Pay */
//				double lossOfPay0 = Double.parseDouble(payslip.getLop());
						double lossOfPay0 = DecimalFormat.getNumberInstance().parse(payslip.getLop()).doubleValue();
						long lossOfPay1 = (new Double(lossOfPay0)).longValue(); // 129
						String lossOfPay = String.valueOf(lossOfPay1);

						/* House Allowance */
//				double hRA0 = Double.parseDouble(payslip.gethRA());
						double hRA0 = DecimalFormat.getNumberInstance().parse(payslip.gethRA()).doubleValue();
						long hRA1 = (new Double(hRA0)).longValue(); // 129
						String hRA = String.valueOf(hRA1);

						/* Employee PF Contribution */
//				double pf0 = Double.parseDouble(payslip.getPf());
						double pf0 = DecimalFormat.getNumberInstance().parse(payslip.getPf()).doubleValue();
						long pf1 = (new Double(pf0)).longValue(); // 129
						String pf = String.valueOf(pf1);

						/* Leave Travel Allowance */
//				double lta0 = Double.parseDouble(payslip.getlTA());
						double lta0 = DecimalFormat.getNumberInstance().parse(payslip.getlTA()).doubleValue();
						long lta1 = (new Double(lta0)).longValue(); // 129
						String lta = String.valueOf(lta1);

						/* TDS */
//				double tds0 = Double.parseDouble(payslip.gettDS());
						double tds0 = DecimalFormat.getNumberInstance().parse(payslip.gettDS()).doubleValue();
						long tds1 = (new Double(tds0)).longValue(); // 129
						String tds = String.valueOf(tds1);

						/* Medical Allowance */
//				double medicalAllownce0 = Double.parseDouble(payslip.getMedicalAll());
						double medicalAllownce0 = DecimalFormat.getNumberInstance().parse(payslip.getMedicalAll())
								.doubleValue();
						long medicalAllownce1 = (new Double(medicalAllownce0)).longValue(); // 129
						String medicalAllownce = String.valueOf(medicalAllownce1);

						/* Mess Payment */
//				double messPay0 = Double.parseDouble(payslip.getMess());
						double messPay0 = DecimalFormat.getNumberInstance().parse(payslip.getMess()).doubleValue();
						long messPay1 = (new Double(messPay0)).longValue(); // 129
						String messPay = String.valueOf(messPay1);

						/* Conveyance Allowance */
//				double conveyanceAllownce0 = Double.parseDouble(payslip.getConveyance());
						double conveyanceAllownce0 = DecimalFormat.getNumberInstance().parse(payslip.getConveyance())
								.doubleValue();
						long conveyanceAllownce1 = (new Double(conveyanceAllownce0)).longValue(); // 129
						String conveyanceAllownce = String.valueOf(conveyanceAllownce1);

						/* Hostel Payment */
//				double hostelPay0 = Double.parseDouble(payslip.getHostel());
						double hostelPay0 = DecimalFormat.getNumberInstance().parse(payslip.getHostel()).doubleValue();
						long hostelPay1 = (new Double(hostelPay0)).longValue(); // 129
						String hostelPay = String.valueOf(hostelPay1);

						/* Special Pay */
//				double specialPay0 = Double.parseDouble(payslip.getSplAllow());
						double specialPay0 = DecimalFormat.getNumberInstance().parse(payslip.getSplAllow())
								.doubleValue();
						long specialPay1 = (new Double(specialPay0)).longValue(); // 129
						String specialPay = String.valueOf(specialPay1);

						/* Staff Benefit Fund */
//				double staffBenefitFund0 = Double.parseDouble(payslip.getsBF());
						double staffBenefitFund0 = DecimalFormat.getNumberInstance().parse(payslip.getsBF())
								.doubleValue();
						long staffBenefitFund1 = (new Double(staffBenefitFund0)).longValue(); // 129
						String staffBenefitFund = String.valueOf(staffBenefitFund1);

						/* Advance/Loan Repayment */
//				double advanceLoan0 = Double.parseDouble(payslip.getAdvanceLoan());
						double advanceLoan0 = DecimalFormat.getNumberInstance().parse(payslip.getAdvanceLoan())
								.doubleValue();
						long advanceLoan1 = (new Double(advanceLoan0)).longValue(); // 129
						String advanceLoan = String.valueOf(advanceLoan1);

						/* Insurance */
//				double insurance0 = Double.parseDouble(payslip.getInsurance());
						double insurance0 = DecimalFormat.getNumberInstance().parse(payslip.getInsurance())
								.doubleValue();
						long insurance1 = (new Double(insurance0)).longValue(); // 129
						String insurance = String.valueOf(insurance1);

						/* Gross Pay (Rounded) error coming for 6,000 (, is get an exception) */
//				double grossPay0 = DecimalFormat.getNumberInstance().parse(payslip.getGross()).doubleValue();
//				long grossPay1 = (new Double(grossPay0)).longValue(); //129
//				String grossPay=String.valueOf(grossPay1);

//				double grossEarning0 = Double.parseDouble(payslip.getGrossEarning());
						double grossEarning0 = DecimalFormat.getNumberInstance().parse(payslip.getGrossEarning())
								.doubleValue();
						long grossEarning1 = (new Double(grossEarning0)).longValue(); // 129
						String grossEarning = String.valueOf(grossEarning1);

						/* Total Deductions (Rounded) */
//				double totalDeduction0 = Double.parseDouble(payslip.getTotalDeduction());
						double totalDeduction0 = DecimalFormat.getNumberInstance().parse(payslip.getTotalDeduction())
								.doubleValue();
						long totalDeduction1 = (new Double(totalDeduction0)).longValue(); // 129
						String totalDeduction = String.valueOf(totalDeduction1);

//				long totalDeduction = (long) totalDeduction1; //129
						/* Net Pay */
//				double netPay0 = Double.parseDouble(payslip.getNetPay());
						double netPay0 = DecimalFormat.getNumberInstance().parse(payslip.getNetPay()).doubleValue();
						long netPay1 = (new Double(netPay0)).longValue(); // 129
						String netPay = String.valueOf(netPay1);

						/* CTC */
//				double cTC0 = DecimalFormat.getNumberInstance().parse(payslip.getcTC()).doubleValue();
//				long cTC1 = (new Double(cTC0)).longValue(); //129
//				String cTC=String.valueOf(cTC1);

						payslip.setBasic(basic);
						payslip.setLop(lossOfPay);
						payslip.sethRA(hRA);
						payslip.setPf(pf);
						payslip.setlTA(lta);
						payslip.settDS(tds);
						payslip.setMedicalAll(medicalAllownce);
						payslip.setMess(messPay);
						payslip.setConveyance(conveyanceAllownce);
						payslip.setHostel(hostelPay);
						payslip.setSplAllow(specialPay);
						payslip.setsBF(staffBenefitFund);
						payslip.setAdvanceLoan(advanceLoan);
						payslip.setInsurance(insurance);
//				payslip.setGross(grossPay);
						payslip.setGrossEarning(grossEarning);
						payslip.setTotalDeduction(totalDeduction);
						payslip.setNetPay(netPay);

						System.out.println(payslip);

						return responseCreation(true, "Success", "Get All User data", null, payslip);

					}
				}
				return responseCreation(false, "Failed", "Data is Null", null, null);

			}

		}

		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@GetMapping("/getAllPaySlipData")
	public ResponseEntity<Object> getAllPaySlipData(@RequestHeader("Authorization") String token,
			@RequestParam("id") Long id) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());
		boolean deleteStatus = true;

		if (logInUser.isPresent()) {

//			String role="EMPLOYEE";

			List<PaySlip> payslipList = paySlipService.findByAllPaySlip(id);

			for (PaySlip payslip : payslipList) {

				if (payslip != null) {
					deleteStatus = false;
				} else {
					deleteStatus = true;
				}
				payslip.setDeleteStatus(deleteStatus);
			}

			return responseCreation(deleteStatus, "Success", "Get All Slip Id", null, null);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@GetMapping("/getEmployeePaySlipReportData")
	public ResponseEntity<Object> getEmployeePaySlipReportData(@RequestHeader("Authorization") String token,
			@RequestParam("paySlipId") Long paySlipId, @RequestParam("monthAndYearID") Long monthAndYearID)
			throws ParseException {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {
			PaySlip payslip = paySlipService.findByIdAndMonthandYaerById(paySlipId, monthAndYearID);
			System.out.println(payslip);

			/* Basic Pay */
//			double basic0 = Double.parseDouble(payslip.getBasic());
			double basic0 = DecimalFormat.getNumberInstance().parse(payslip.getBasic()).doubleValue();
			long basic1 = (new Double(basic0)).longValue(); // 129
			String basic = String.valueOf(basic1);

			/* Loss of Pay */
//			double lossOfPay0 = Double.parseDouble(payslip.getLop());
			double lossOfPay0 = DecimalFormat.getNumberInstance().parse(payslip.getLop()).doubleValue();
			long lossOfPay1 = (new Double(lossOfPay0)).longValue(); // 129
			String lossOfPay = String.valueOf(lossOfPay1);

			/* House Allowance */
//			double hRA0 = Double.parseDouble(payslip.gethRA());
			double hRA0 = DecimalFormat.getNumberInstance().parse(payslip.gethRA()).doubleValue();
			long hRA1 = (new Double(hRA0)).longValue(); // 129
			String hRA = String.valueOf(hRA1);

			/* Employee PF Contribution */
//			double pf0 = Double.parseDouble(payslip.getPf());
			double pf0 = DecimalFormat.getNumberInstance().parse(payslip.getPf()).doubleValue();
			long pf1 = (new Double(pf0)).longValue(); // 129
			String pf = String.valueOf(pf1);

			/* Leave Travel Allowance */
//			double lta0 = Double.parseDouble(payslip.getlTA());
			double lta0 = DecimalFormat.getNumberInstance().parse(payslip.getlTA()).doubleValue();
			long lta1 = (new Double(lta0)).longValue(); // 129
			String lta = String.valueOf(lta1);

			/* TDS */
//			double tds0 = Double.parseDouble(payslip.gettDS());
			double tds0 = DecimalFormat.getNumberInstance().parse(payslip.gettDS()).doubleValue();
			long tds1 = (new Double(tds0)).longValue(); // 129
			String tds = String.valueOf(tds1);

			/* Medical Allowance */
//			double medicalAllownce0 = Double.parseDouble(payslip.getMedicalAll());
			double medicalAllownce0 = DecimalFormat.getNumberInstance().parse(payslip.getMedicalAll()).doubleValue();
			long medicalAllownce1 = (new Double(medicalAllownce0)).longValue(); // 129
			String medicalAllownce = String.valueOf(medicalAllownce1);

			/* Mess Payment */
//			double messPay0 = Double.parseDouble(payslip.getMess());
			double messPay0 = DecimalFormat.getNumberInstance().parse(payslip.getMess()).doubleValue();
			long messPay1 = (new Double(messPay0)).longValue(); // 129
			String messPay = String.valueOf(messPay1);

			/* Conveyance Allowance */
//			double conveyanceAllownce0 = Double.parseDouble(payslip.getConveyance());
			double conveyanceAllownce0 = DecimalFormat.getNumberInstance().parse(payslip.getConveyance()).doubleValue();
			long conveyanceAllownce1 = (new Double(conveyanceAllownce0)).longValue(); // 129
			String conveyanceAllownce = String.valueOf(conveyanceAllownce1);

			/* Hostel Payment */
//			double hostelPay0 = Double.parseDouble(payslip.getHostel());
			double hostelPay0 = DecimalFormat.getNumberInstance().parse(payslip.getHostel()).doubleValue();
			long hostelPay1 = (new Double(hostelPay0)).longValue(); // 129
			String hostelPay = String.valueOf(hostelPay1);

			/* Special Pay */
//			double specialPay0 = Double.parseDouble(payslip.getSplAllow());
			double specialPay0 = DecimalFormat.getNumberInstance().parse(payslip.getSplAllow()).doubleValue();
			long specialPay1 = (new Double(specialPay0)).longValue(); // 129
			String specialPay = String.valueOf(specialPay1);

			/* Staff Benefit Fund */
//			double staffBenefitFund0 = Double.parseDouble(payslip.getsBF());
			double staffBenefitFund0 = DecimalFormat.getNumberInstance().parse(payslip.getsBF()).doubleValue();
			long staffBenefitFund1 = (new Double(staffBenefitFund0)).longValue(); // 129
			String staffBenefitFund = String.valueOf(staffBenefitFund1);

			/* Advance/Loan Repayment */
//			double advanceLoan0 = Double.parseDouble(payslip.getAdvanceLoan());
			double advanceLoan0 = DecimalFormat.getNumberInstance().parse(payslip.getAdvanceLoan()).doubleValue();
			long advanceLoan1 = (new Double(advanceLoan0)).longValue(); // 129
			String advanceLoan = String.valueOf(advanceLoan1);

			/* Insurance */
//			double insurance0 = Double.parseDouble(payslip.getInsurance());
			double insurance0 = DecimalFormat.getNumberInstance().parse(payslip.getInsurance()).doubleValue();
			long insurance1 = (new Double(insurance0)).longValue(); // 129
			String insurance = String.valueOf(insurance1);

			/* Gross Pay (Rounded) error coming for 6,000 (, is get an exception) */
//			double grossPay0 = DecimalFormat.getNumberInstance().parse(payslip.getGross()).doubleValue();
//			long grossPay1 = (new Double(grossPay0)).longValue(); //129
//			String grossPay=String.valueOf(grossPay1);

//			double grossEarning0 = Double.parseDouble(payslip.getGrossEarning());
			double grossEarning0 = DecimalFormat.getNumberInstance().parse(payslip.getGrossEarning()).doubleValue();
			long grossEarning1 = (new Double(grossEarning0)).longValue(); // 129
			String grossEarning = String.valueOf(grossEarning1);

			/* Total Deductions (Rounded) */
//			double totalDeduction0 = Double.parseDouble(payslip.getTotalDeduction());
			double totalDeduction0 = DecimalFormat.getNumberInstance().parse(payslip.getTotalDeduction()).doubleValue();
			long totalDeduction1 = (new Double(totalDeduction0)).longValue(); // 129
			String totalDeduction = String.valueOf(totalDeduction1);

//			long totalDeduction = (long) totalDeduction1; //129
			/* Net Pay */
//			double netPay0 = Double.parseDouble(payslip.getNetPay());
			double netPay0 = DecimalFormat.getNumberInstance().parse(payslip.getNetPay()).doubleValue();
			long netPay1 = (new Double(netPay0)).longValue(); // 129
			String netPay = String.valueOf(netPay1);

			/* CTC */
//			double cTC0 = DecimalFormat.getNumberInstance().parse(payslip.getcTC()).doubleValue();
//			long cTC1 = (new Double(cTC0)).longValue(); //129
//			String cTC=String.valueOf(cTC1);

			// DOJ
			String ds1 = payslip.getUser().getDoj();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
			String ds2 = sdf2.format(sdf1.parse(ds1));

			payslip.setBasic(basic);
			payslip.setLop(lossOfPay);
			payslip.sethRA(hRA);
			payslip.setPf(pf);
			payslip.setlTA(lta);
			payslip.settDS(tds);
			payslip.setMedicalAll(medicalAllownce);
			payslip.setMess(messPay);
			payslip.setConveyance(conveyanceAllownce);
			payslip.setHostel(hostelPay);
			payslip.setSplAllow(specialPay);
			payslip.setsBF(staffBenefitFund);
			payslip.setAdvanceLoan(advanceLoan);
			payslip.setInsurance(insurance);
//			payslip.setGross(grossPay);
			payslip.setGrossEarning(grossEarning);
			payslip.setTotalDeduction(totalDeduction);
			payslip.setNetPay(netPay);
			payslip.getUser().setDoj(ds2);
//			payslip.setcTC(cTC);

			return responseCreation(true, "Success", "Get PaySlip data", null, payslip);

		}

		return responseCreation(false, "Faild", "User Can't be null", null, null);

	}

	@DeleteMapping("/deleteByPayslipId")
	public ResponseEntity<Object> deleteByUser(@RequestHeader("Authorization") String token, @RequestParam Long id) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			paySlipService.deletePayslipById(id);

			return responseCreation(true, "Success", "Delete User Payslip successfully", null, null);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	private ResponseEntity<Object> responseCreation(boolean status, String message, String description,
			List<PaySlip> paySlipList, PaySlip payslip) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (paySlipList != null) {
			response.setPaySlips(paySlipList);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "paySlips" });
		} else if (payslip != null) {
			response.setPaySlipsView(payslip);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "paySlipsView" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

}
