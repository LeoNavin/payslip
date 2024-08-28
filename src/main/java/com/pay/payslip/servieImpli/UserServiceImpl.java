package com.pay.payslip.servieImpli;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.User;
import com.pay.payslip.repository.UserRepository;
import com.pay.payslip.servie.UserService;
/**
 * @author Leo Navin
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public void updateUser(User user) {
		userRepository.saveAndFlush(user);
	}

	@Override
	public Optional<User> findByAssociateId(String associateId) {
		return userRepository.getByAssociateId(associateId);
	}

	@Override
	public List<User> findByAlluser(Long id) {
		return userRepository.findByUserId(id);
	}

	@Override
	public User getByAssociateId(String employeeAssociateId) {
		return userRepository.findByAssociateId(employeeAssociateId);
	}

	@Override
	public User getByFirstname(String employeeFirstname) {
		return userRepository.findByFirstname(employeeFirstname);
	}



	@Override
	public User getByEmail(String employeeEmail) {
		return userRepository.findByEmail(employeeEmail);
	}

	@Override
	public void addUser(User employee) {
		userRepository.saveAndFlush(employee);

	}

	@Override
	public void getByUserId(Long id) {
		userRepository.deleteById(id);

	}

	@Override
	public Optional<User> findByUserId(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findByUAN(String employeeUAN) {
		// TODO Auto-generated method stub
		return userRepository.findByUan(employeeUAN);
	}

	@Override
	public Optional<User> findByBankAccountNumber(String employeeBankAccountNumber) {
		return userRepository.findByBankAccountNumber(employeeBankAccountNumber);
	}

	@Override
	public User findByAllUseremail(String email) {
		return userRepository.findByEmail(email);
	}

	

	



}
