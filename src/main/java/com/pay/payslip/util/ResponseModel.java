package com.pay.payslip.util;

import java.io.Reader;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.pay.payslip.dto.MobileMonthAndPayslipDTO;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.Month;
import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;
import com.pay.payslip.model.Role;
import com.pay.payslip.model.User;
/**
 * @author Leo Navin
 *
 */
@JsonFilter("ResponseModelFilter")
public class ResponseModel {

	private boolean status;

	private ExceptionBean information;

	private Role role;



	private List<Role> roles;

	private List<User> users;
	private List<MonthAndYear> monthandyear;
	private List<Month> month;
	private MonthAndYear monandYear;

	
	private Reader files;

	private String file;
	private List<String> messages;
	
	private String otp;

	private String value;


	private List<PaySlip> paySlips;
	private PaySlip paySlipsView;

	
	private MobileMonthAndPayslipDTO mobileMonthAndPayslipDTO;

	private User user;

	public ResponseModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ResponseModel [status=" + status + ", information=" + information + ", role=" + role + ", roles="
				+ roles + ", users=" + users + ", monthandyear=" + monthandyear + ", month=" + month + ", monandYear="
				+ monandYear + ", files=" + files + ", file=" + file + ", messages=" + messages + ", otp=" + otp
				+ ", value=" + value + ", paySlips=" + paySlips + ", paySlipsView=" + paySlipsView
				+ ", mobileMonthAndPayslipDTO=" + mobileMonthAndPayslipDTO + ", user=" + user + "]";
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ExceptionBean getInformation() {
		return information;
	}

	public void setInformation(ExceptionBean information) {
		this.information = information;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	public List<Month> getMonth() {
		return month;
	}

	public void setMonth(List<Month> month) {
		this.month = month;
	}

	public List<MonthAndYear> getMonthandyear() {
		return monthandyear;
	}

	public void setMonthandyear(List<MonthAndYear> monthandyear) {
		this.monthandyear = monthandyear;
	}

	public MonthAndYear getMonandYear() {
		return monandYear;
	}

	public void setMonandYear(MonthAndYear monandYear) {
		this.monandYear = monandYear;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	

	

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public List<PaySlip> getPaySlips() {
		return paySlips;
	}

	public void setPaySlips(List<PaySlip> paySlips) {
		this.paySlips = paySlips;
	}

	public PaySlip getPaySlipsView() {
		return paySlipsView;
	}

	public void setPaySlipsView(PaySlip paySlipsView) {
		this.paySlipsView = paySlipsView;
	}

	public Reader getFiles() {
		return files;
	}

	public void setFiles(Reader files) {
		this.files = files;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MobileMonthAndPayslipDTO getMobileMonthAndPayslipDTO() {
		return mobileMonthAndPayslipDTO;
	}

	public void setMobileMonthAndPayslipDTO(MobileMonthAndPayslipDTO mobileMonthAndPayslipDTO) {
		this.mobileMonthAndPayslipDTO = mobileMonthAndPayslipDTO;
	}


	
	
	
	
	
	
}
