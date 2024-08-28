package com.pay.payslip.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pay.payslip.repository.RoleRepository;
import com.pay.payslip.repository.UserRepository;
import com.pay.payslip.servieImpli.UserPrincipalDetailsServiceImpl;

/**
 * @author Leo Navin
 *
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserPrincipalDetailsServiceImpl userPrincipalDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter2 = new JwtAuthenticationFilter(authenticationManager(),
				userRepository);
		jwtAuthenticationFilter2.setFilterProcessesUrl("/user/login");
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilter(jwtAuthenticationFilter2)
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)).authorizeRequests()
				.antMatchers(HttpMethod.POST, "/user/forgotpassword","/forgetPassword").permitAll()
				.antMatchers(HttpMethod.POST, "/user/login","/user/save").permitAll().antMatchers(HttpMethod.POST, "/user")
				.permitAll()
				.antMatchers("http://localhost:4200/**").permitAll()
				.antMatchers("/resources/**","/vericea/static/report/**").permitAll()
				.antMatchers("/**").permitAll() 
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().antMatchers("/**")
				.access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
				//.hasAnyAuthority("ADMIN ")
				//.hasRole("ADMIN")
				.anyRequest().authenticated();
//        http.authorizeRequests().antMatchers("/webjars/**").permitAll();
//        http.authorizeRequests().antMatchers("/resources/**","/vericea/static/report/**").permitAll();

		http.cors();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001/**"));

//		configuration.setAllowedMethods(List.of("http://localhost:3001/**"));
		
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow swagger to be accessed without authentication
		web.ignoring().antMatchers("/v2/api-docs").antMatchers("/swagger-resources/**").antMatchers("/swagger-ui.html")
				.antMatchers("/configuration/**").antMatchers("/webjars/**").antMatchers("http://localhost:3001/**")
				.antMatchers("/public");
	}
}
