package com.pay.payslip.dto;

import java.util.List;

import com.pay.payslip.model.MonthAndYear;
/**
 * @author Leo Navin
 *
 */
public class MonthAndYearDTO {

	private List<MonthAndYear> monthAndYear;
	
	

	private boolean status;

	@Override
	public String toString() {
		return "MonthAndYearDTO [monthAndYear=" + monthAndYear + ", status=" + status + "]";
	}

	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void setMonthAndYear(List<MonthAndYear> monthAndYear) {
		this.monthAndYear = monthAndYear;
	}


	public List<MonthAndYear> getMonthAndYear() {
		return monthAndYear;
	}
}
