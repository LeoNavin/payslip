package com.pay.payslip.dto;

import java.util.List;

import com.pay.payslip.model.MonthAndYear;
import com.pay.payslip.model.PaySlip;

public class MobileMonthAndPayslipDTO {
	private List<MonthAndYear> monthAndYears;
	private List<PaySlip> paySlipList;

	@Override
	public String toString() {
		return "MobileMonthAndPayslipDTO [monthAndYears=" + monthAndYears + ", paySlipList=" + paySlipList + "]";
	}

	public List<MonthAndYear> getMonthAndYears() {
		return monthAndYears;
	}

	public void setMonthAndYears(List<MonthAndYear> monthAndYears) {
		this.monthAndYears = monthAndYears;
	}

	

	public List<PaySlip> getPaySlipList() {
		return paySlipList;
	}

	public void setPaySlipList(List<PaySlip> paySlipList) {
		this.paySlipList = paySlipList;
	}

	public MobileMonthAndPayslipDTO() {
		// TODO Auto-generated constructor stub
	}
}
