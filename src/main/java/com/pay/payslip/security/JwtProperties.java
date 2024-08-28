package com.pay.payslip.security;
/**
 * @author Leo Navin
 *
 */
public class JwtProperties {

	public static final String SECRET = "jwttoken";
	public static final int EXPIRATION = 84600000;
	public static final String PREFIX = "Bearer ";
	public static final String HEADER = "Authorization";
}
