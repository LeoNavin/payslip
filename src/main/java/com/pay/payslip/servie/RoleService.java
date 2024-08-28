package com.pay.payslip.servie;

import java.util.Optional;

import com.pay.payslip.model.Role;


/**
 * @author Leo Navin
 *
 */
public interface RoleService {

	Role saveUserRole(Role role);

	Optional<Role> getById(Long userRole);

//	Optional<Role> findById(Long id);
//
//	Optional<Role> findByRole(String role);
//
//	void deleteUserRoleById(Long id);
//
//	List<Role> findAll();
//
//	Optional<Role> getById(Long userRole);

	
	//set here Role ID=2
////	Optional<Role> findByRole(Object setRoleId);
//
//	Optional<Role> findByRole(Object setRoleId);

	
}
