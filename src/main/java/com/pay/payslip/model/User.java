/**
 * 
 */
package com.pay.payslip.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leo Navin
 *
 */
@Entity
@Table(name = "tbl_user")
public class User extends AbstractAuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String associateId;
	private String firstname;
	private String lastname;
	private String dateOfBirth;
	private String email;
	private String phoneNumber;
	private String username;
	private String password;
	private String profilePath;

	private String uan;/* Universal Account Number For EPF */
	private String designation;
	private String gender;
	private String unit;
	private String doj;/* Date of Joining */
	private String bankName;
	private String bankAccountNumber;

	private boolean status;
	private String permissions;

	@ManyToOne
	private Role role;

	@Transient
	private Long roleId;

	@Transient
	private boolean deleteStatus;

	@Override
	public String toString() {
		return "User [id=" + id + ", associateId=" + associateId + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", dateOfBirth=" + dateOfBirth + ", email=" + email + ", phoneNumber=" + phoneNumber + ", username="
				+ username + ", password=" + password + ", profilePath=" + profilePath + ", uan=" + uan
				+ ", designation=" + designation + ", gender=" + gender + ", unit=" + unit + ", doj=" + doj
				+ ", bankName=" + bankName + ", bankAccountNumber=" + bankAccountNumber + ", status=" + status
				+ ", permissions=" + permissions + ", role=" + role + ", roleId=" + roleId + ", deleteStatus="
				+ deleteStatus + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public String getAssociateId() {
		return associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@ApiModelProperty(hidden = true)
	public List<String> getPermissionList() {
		if (this.permissions.length() > 0) {
			return Arrays.asList(this.permissions.split(","));
		}
		return new ArrayList<>();
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getUan() {
		return uan;
	}

	public void setUan(String uan) {
		this.uan = uan;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getProfilePath() {
		return profilePath;
	}

	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}

	// This line Useing for Startup admin Creating
	public User(String firstname, String lastname, String email, String phoneNumber, String username, String password,
			boolean status, Role role) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.username = username;
		this.password = password;
		this.status = status;
		this.role = role;
	}

	// This line Using for User Controller Save User Data
	public User(String associateId, String firstname, String dateOfBirth, String email, String phoneNumber,
			String password, String uan, String designation, String gender, String unit, String doj, String bankName,
			String bankAccountNumber, boolean status, String permissions, Role role) {
		super();
		this.associateId = associateId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.uan = uan;
		this.designation = designation;
		this.gender = gender;
		this.unit = unit;
		this.doj = doj;
		this.bankName = bankName;
		this.bankAccountNumber = bankAccountNumber;
		this.status = status;
		this.permissions = permissions;
		this.role = role;
	}

}
