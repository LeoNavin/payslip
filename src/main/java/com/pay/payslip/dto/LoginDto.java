package com.pay.payslip.dto;

import io.swagger.annotations.ApiModelProperty;
/**
 * @author Leo Navin
 *
 */
public class LoginDto {
	

	@ApiModelProperty(position = 1 )
	private String username;
	@ApiModelProperty(position = 2 )
	private String password;
	@Override
	public String toString() {
		return "LoginDto [username=" + username + ", password=" + password + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public LoginDto() {
		// TODO Auto-generated constructor stub
	}

}
