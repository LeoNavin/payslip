package com.pay.payslip.util;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.User;
/**
 * @author Leo Navin
 *
 */
public class ResponseJson {

	private ResponseModel response = new ResponseModel();

	private MappingJacksonValue mappingJacksonValue;

	public ResponseEntity<Object> responseCreation(Boolean status, String message, String description,
			List<User> users, User user) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (users != null) {
			response.setUsers(users);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "users" });
		} else if (user != null) {
			response.setUser(user);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "user" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

	
	

	

	

	


	

	
	

	

	

	
	
	
}




//public ResponseEntity<Object> responseCreation(boolean status, String message, String description,
//		List<Child> child) {
//	response.setStatus(status);
//	response.setInformation(new ExceptionBean(new Date(), message, description));
//	if (child != null) {
//		response.setChild(child);
//		mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "child" });
//	}
//	return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);



