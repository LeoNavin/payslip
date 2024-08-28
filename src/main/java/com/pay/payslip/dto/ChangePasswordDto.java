/**
 * 
 */
package com.pay.payslip.dto;

/**
 * @author Leo Navin
 *
 */
public class ChangePasswordDto {

	private String oldPassword;
	private String confirmPassword;
	
	
	@Override
	public String toString() {
		return "ChangePasswordDto [oldPassword=" + oldPassword + ", confirmPassword=" + confirmPassword + "]";
	}

	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	
}
