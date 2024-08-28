/**
 * 
 */
package com.pay.payslip.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author Leo Navin
 *
 */
@Configuration
public class BcryptPasswordEncoder extends BCrypt{
	
	public String encode(String password) {
	    return hashpw(password , gensalt());
	}
	
	public boolean checkPassword(String rawPassword, String encodedPassword) {
        return checkpw(rawPassword, encodedPassword);
    }

}

