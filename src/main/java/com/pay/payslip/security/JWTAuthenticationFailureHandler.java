/**
 * 
 */
package com.pay.payslip.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.dto.LoginDto;

/**
 * @author Leo Navin
 *
 */
public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(200);
		response.setContentType("application/json");
		System.out.println(json());
		response.getWriter().append(json());
			
	}
	private String json() throws JsonParseException {
		
		long date = new Date().getTime();
		

		String response = "{\"timestamp\": " + date + ", " + "\"code\": 401, " + "\"error\": \"Unauthorized\", "
				+ "\"message\": \"Authentication failed: bad credentials\"," + "\"path\": \"/login\"}";

		String jsonResponse = "{\"response\": " + response + ", " + "\"status\": " + false + "}";

		return jsonResponse;
	}
}
