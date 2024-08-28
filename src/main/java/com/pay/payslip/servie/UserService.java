package com.pay.payslip.servie;

import java.util.List;
import java.util.Optional;

import com.pay.payslip.model.User;

/**
 * @author Leo Navin
 *
 */

public interface UserService {

	Optional<User> findByUsername(String username);

//	Optional<User> findById(Long id);

	User saveUser(User user);
	void updateUser(User user);

	Optional<User> findByAssociateId(String associateId);

	List<User> findByAlluser(Long id);

	User getByAssociateId(String employeeAssociateId);

	User getByFirstname(String employeeFirstname);


	User getByEmail(String employeeEmail);

	void addUser(User employee);

	void getByUserId(Long id);

	Optional<User> findByUserId(Long id);

	Optional<User> findByUAN(String employeeUAN);

	Optional<User> findByBankAccountNumber(String employeeBankAccountNumber);


	User findByAllUseremail(String email);

	Optional<User> findById(Long userId);



	





	

	
}