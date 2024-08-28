package com.pay.payslip.servieImpli;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.User;
import com.pay.payslip.model.UserPrincipal;
import com.pay.payslip.repository.UserRepository;

/**
 * @author Leo Navin
 *
 */

@Service
public class UserPrincipalDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	public UserPrincipalDetailsServiceImpl() {
		super();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Optional<User> user = userRepository.findByUsername(username);
			if(user.isPresent()) {
				UserPrincipal userPrincipal = new UserPrincipal(user.get());
				System.out.println(userPrincipal.getAuthorities());
				return userPrincipal;
			}
		} catch (Exception e) {
		}	
		throw new UsernameNotFoundException(username);
	
	}

}
