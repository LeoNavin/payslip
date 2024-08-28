/**
 * 
 */
package com.pay.payslip.dto;

/**
 * @author Leo Navin
 *
 */
public class ForgetPasswordDto {

	public String email;

	@Override
	public String toString() {
		return "ForgetPasswordDto [email=" + email + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
