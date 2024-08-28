package com.pay.payslip.servieImpli;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.Role;
import com.pay.payslip.repository.RoleRepository;
import com.pay.payslip.servie.RoleService;

/**
 * @author Leo Navin
 *
 */

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role saveUserRole(Role role) {
		return roleRepository.saveAndFlush(role);
	}

	@Override
	public Optional<Role> getById(Long userRole) {
		return roleRepository.findById(userRole);
	}

//	@Override
//	public Optional<Role> findById(Long id) {
//		return roleRepository.findById(id);
//	}
//
//	@Override
//	public Optional<Role> findByRole(String role) {
//		return roleRepository.findByRole(role);
//	}
//
//	@Override
//	public void deleteUserRoleById(Long id) {
//		roleRepository.deleteById(id);
//	}
//
//	@Override
//	public List<Role> findAll() {
//		return roleRepository.findAll();
//	}
//
//	@Override
//	public Optional<Role> getById(Long userRole) {
//		
//		return roleRepository.findById(userRole);
//	}
	
	
}
