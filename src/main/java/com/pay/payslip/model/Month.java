/**
 * 
 */
package com.pay.payslip.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Leo Navin
 *
 */
@Entity
@Table(name = "tbl_month")
public class Month extends AbstractAuditingEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String month;
	@Override
	public String toString() {
		return "Month [id=" + id + ", month=" + month + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	public Month() {
		// TODO Auto-generated constructor stub
	}
	
	public Month(String month) {
		super();
		this.month = month;
	}

}
