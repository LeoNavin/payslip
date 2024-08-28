package com.pay.payslip.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pay.payslip.exception.InvalidTokenorExpiredException;
import com.pay.payslip.model.User;
import com.pay.payslip.model.UserPrincipal;
import com.pay.payslip.repository.UserRepository;
/**
 * @author Leo Navin
 *
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(JwtProperties.HEADER);
		if (header == null || !header.startsWith(JwtProperties.PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		Authentication authentication = getUsernamePasswordAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtProperties.HEADER).replace(JwtProperties.PREFIX, "");
		if (token != null) {
			String username = null;
			try {
				username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes())).build().verify(token)
						.getSubject();
			} catch (JWTVerificationException | IllegalArgumentException e) {
				return null;
			}
			if (username != null) {
				Optional<User> user = userRepository.findByUsername(username);
				if (user.isPresent()) {
					UserPrincipal userPrincipal = new UserPrincipal(user.get());
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
							userPrincipal.getAuthorities());
					return auth;
				}
				return null;
			} else {
				throw new InvalidTokenorExpiredException("Token is not valid!");
			}
		}
		return null;
	}
	
	

}

