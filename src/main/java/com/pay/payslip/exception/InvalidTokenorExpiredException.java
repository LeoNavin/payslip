package com.pay.payslip.exception;
/**
 * @author Leo Navin
 *
 */
public class InvalidTokenorExpiredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenorExpiredException(String message) {
		super(message);
	}
}
