/**
 * 
 */
package com.pay.payslip.dto;

import javax.mail.Multipart;

import com.pay.payslip.model.User;

/**
 * @author Leo Navin
 *
 */
public class UserSaveDTO {

	private User user;
	
	private Multipart profile;

	@Override
	public String toString() {
		return "UserSaveDTO [user=" + user + ", profile=" + profile + "]";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Multipart getProfile() {
		return profile;
	}

	public void setProfile(Multipart profile) {
		this.profile = profile;
	}
	
	
	public UserSaveDTO() {
		// TODO Auto-generated constructor stub
	}
	
}
