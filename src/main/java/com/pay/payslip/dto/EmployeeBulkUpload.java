/**
 * 
 */
package com.pay.payslip.dto;

import java.util.List;

import com.pay.payslip.model.User;

/**
 * @author Leo Navin
 *
 */
public class EmployeeBulkUpload {

	private Boolean error;
	private Integer errorCode;
	private List<String> errorMessage;
	private User user;
//	private Long associateId;
//	private String firstname;
//	private String lastname;
//	private String email;
//	private String phoneNumber;
//	private String username;
//	private String password;

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(List<String> errorMessage) {
		this.errorMessage = errorMessage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "EmployeeBulkUpload [error=" + error + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ ", user=" + user + "]";
	}

}
