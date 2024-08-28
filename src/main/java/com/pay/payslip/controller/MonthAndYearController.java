/**
 * 
 */
package com.pay.payslip.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.Month;
import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;
import com.pay.payslip.model.User;
import com.pay.payslip.servie.MonthAndYearService;
import com.pay.payslip.servie.MonthService;
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
@RequestMapping(value = "/monthAndYear")
public class MonthAndYearController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private MonthService monthService;
	@Autowired
	private MonthAndYearService monthAndYearService;

	private ResponseModel response = new ResponseModel();

//	private ResponseJson responseJson = new ResponseJson();

	private MappingJacksonValue mappingJacksonValue;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

	@Autowired
	private PaySlipService paySlipService;

	@PostMapping("/monthandyearsave")
	public ResponseEntity<Object> monthAndYearSave(@RequestHeader("Authorization") String token,
			@RequestBody MonthAndYear monthAndYear) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			Optional<Month> monthId = monthService.findByMonthId(monthAndYear.getMonthId());

			if (!monthId.isEmpty()) {
				if (monthAndYear != null && monthAndYear.getYear() != null) {

					if (monthAndYear.getId() != null) {
						Optional<MonthAndYear> monthAndYear1 = monthAndYearService.getByMonthAndYear(monthAndYear.getMonthId(),
								monthAndYear.getYear());
						if(monthAndYear1.isEmpty()) {
							monthAndYear.setMonth(monthId.get());
							monthAndYear.setLastModifiedBy("ADMIN");
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							monthAndYear.setLastModifiedDate(sdf1.format(new Date()));
							monthAndYearService.saveMonthAndYear(monthAndYear);

							return responseCreation(true, "Success", "Updated is successfully", null, null);

						}else {
							return responseCreation(false, "Failed", "Month And Year Already Exist", null, null);

						}
						
					
					} else if (monthAndYear.getId() == null) {

						Optional<MonthAndYear> monthAndYear1 = monthAndYearService.getByMonthAndYear(monthAndYear.getMonthId(),
								monthAndYear.getYear());

						if (monthAndYear1.isEmpty()) {

							monthAndYear.setMonth(monthId.get());
							monthAndYear.setCreatedBy("ADMIN");
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							monthAndYear.setCreatedDate(sdf1.format(new Date()));
							monthAndYearService.saveMonthAndYear(monthAndYear);

							return responseCreation(true, "Success", "Save successfully", null, null);
						}

						return responseCreation(false, "Failed", "Month And Year Already Exist", null, null);

					} else {
						return responseCreation(false, "Failed", "Id Can't be Null", null, null);
					}
				} else {
					return responseCreation(false, "Failed", "Month and Year Can't be Empty", null, null);
				}

			}
			return responseCreation(false, "Failed", "Admin username and password Faild ", null, null);
		}

		return responseCreation(false, "Failed", "associateId already exit ", null, null);

	}

	@GetMapping("/getAllMonthandyear")
	public ResponseEntity<Object> getAllUser(@RequestHeader("Authorization") String token) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {

			if (logInUser.get().getRole().getRole().equals("ADMIN")) {

				List<MonthAndYear> monthAndYearList = monthAndYearService.findAllMonthAndYear();

				for (MonthAndYear monthandYear : monthAndYearList) {
					List<PaySlip> paySlipId = paySlipService.findByAllPaySlip(monthandYear.getId());
					System.out.println(paySlipId);

					if (!paySlipId.isEmpty()) {
						monthandYear.setPaySlipStatus(false);
					} else {
						monthandYear.setPaySlipStatus(true);
					}
				}
				System.out.println(monthAndYearList);

				return responseCreation(true, "Success", "Get All User data", monthAndYearList, null);

			} else {
				List<MonthAndYear> monthAndYearList = monthAndYearService.findAllMonthAndYear();

				for (MonthAndYear monthandYear : monthAndYearList) {
					PaySlip paySlipId = paySlipService.findByAssociateIdAndMonthId(logInUser.get().getAssociateId(), monthandYear.getId());
					System.out.println(paySlipId);

					if ( paySlipId == null) {
						monthandYear.setPaySlipStatus(true);
					} else {
						monthandYear.setPaySlipStatus(false);
					}
				}
				System.out.println(monthAndYearList);

				return responseCreation(true, "Success", "Get All User data", monthAndYearList, null);
			}
		}

		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@GetMapping("/getMonthById")
	public ResponseEntity<Object> getMonthById(@RequestHeader("Authorization") String token, @RequestParam Long id) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {

			MonthAndYear mAndYear = monthAndYearService.getMonthById(id);

			return responseCreation(true, "Success", "Get Month Only", mAndYear);

		}

		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@DeleteMapping("/deleteByMonandYearId")
	public ResponseEntity<Object> deleteByMonandYearId(@RequestHeader("Authorization") String token,
			@RequestParam("id") Long id) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			if (id != null) {
			
				monthAndYearService.deleteById(id);

				return responseCreation(true, "Success", "Get All User data", null, null);

			}
			return responseCreation(false, "Failed", "User can't be null!", null, null);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	private ResponseEntity<Object> responseCreation(boolean status, String message, String description,
			List<MonthAndYear> monthAndYearList, MonthAndYear mAndYear) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (monthAndYearList != null) {
			response.setMonthandyear(monthAndYearList);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "monthandyear" });
		} else if (mAndYear != null) {
			response.setMonandYear(mAndYear);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "monandYear" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

	private ResponseEntity<Object> responseCreation(boolean status, String message, String description,
			MonthAndYear mAndYear) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (mAndYear != null) {
			response.setMonandYear(mAndYear);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "monandYear" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

}
