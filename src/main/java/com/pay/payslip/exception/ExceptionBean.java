package com.pay.payslip.exception;

import java.util.Date;
/**
 * @author Leo Navin
 *
 */
public class ExceptionBean {

	private Date timestamp;
	private String message;
	private String description;
	
	public ExceptionBean() {

	}

	public ExceptionBean(Date timestamp, String message, String description) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.description = description;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ExceptionBean [timestamp=" + timestamp + ", message=" + message + ", description=" + description + "]";
	}
	
	
}
