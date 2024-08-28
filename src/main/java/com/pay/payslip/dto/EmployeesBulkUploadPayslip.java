/**
 * 
 */
package com.pay.payslip.dto;

import java.util.List;

import com.pay.payslip.model.PaySlip;

/**
 * @author Leo Navin
 *
 */
public class EmployeesBulkUploadPayslip {
	
	private Boolean error;
	private Integer errorCode;
	private List<String> errorMessage;
	private PaySlip paySlip;

	@Override
	public String toString() {
		return "EmployeesBulkUploadPayslip [error=" + error + ", errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + ", paySlip=" + paySlip + "]";
	}
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
	public PaySlip getPaySlip() {
		return paySlip;
	}
	public void setPaySlip(PaySlip paySlip) {
		this.paySlip = paySlip;
	}
	

}
