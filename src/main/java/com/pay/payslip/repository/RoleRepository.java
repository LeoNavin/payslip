/**
 * 
 */
package com.pay.payslip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pay.payslip.model.Role;



/**
 * @author Leo Navin
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRole(String role);

//	Optional<Role> findByRole(Object setRoleId);
}
