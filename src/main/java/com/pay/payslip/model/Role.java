package com.pay.payslip.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author Leo Navin
 *
 */
@Entity
@Table(name = "tbl_role")
public class Role extends AbstractAuditingEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String role;
	private String description;
	private boolean status;
	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + ", description=" + description + ", status=" + status + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Role() {
		// TODO Auto-generated constructor stub
	}
	public Role(Long id, String role, String description, boolean status) {
		super();
		this.id = id;
		this.role = role;
		this.description = description;
		this.status = status;
	}
	public Role(String role, String description, boolean status) {
		super();
		this.role = role;
		this.description = description;
		this.status = status;
	}
	
	
}
