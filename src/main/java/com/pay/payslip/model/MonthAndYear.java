/**
 * 
 */
package com.pay.payslip.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Leo Navin
 *
 */
@Entity
@Table(name = "tbl_month_and_year")
public class MonthAndYear extends AbstractAuditingEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String year;

	@ManyToOne
	private Month month;

	@Transient
	private Long monthId;

	@Transient
	private boolean paySlipStatus;

	@Override
	public String toString() {
		return "MonthAndYear [id=" + id + ", year=" + year + ", month=" + month + ", monthId=" + monthId
				+ ", paySlipStatus=" + paySlipStatus + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Long getMonthId() {
		return monthId;
	}

	public void setMonthId(Long monthId) {
		this.monthId = monthId;
	}



	public boolean isPaySlipStatus() {
		return paySlipStatus;
	}

	public void setPaySlipStatus(boolean paySlipStatus) {
		this.paySlipStatus = paySlipStatus;
	}

	public MonthAndYear(String year, Month month, Long monthId) {
		super();
		this.year = year;
		this.month = month;
		this.monthId = monthId;
	}

	public MonthAndYear(Long id, String year, Month month, Long monthId) {
		super();
		this.id = id;
		this.year = year;
		this.month = month;
		this.monthId = monthId;
	}

	public MonthAndYear(String year, Month month) {
		super();
		this.year = year;
		this.month = month;
	}

	public MonthAndYear() {
		// TODO Auto-generated constructor stub
	}
}
