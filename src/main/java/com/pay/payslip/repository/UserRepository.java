package com.pay.payslip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pay.payslip.model.User;
/**
 * @author Leo Navin
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {


	public Optional<User> findByUsername(String username);

//	@Query( "SELECT u FROM User u WHERE u.id != :id")
	@Query("SELECT u FROM User u WHERE u.id != :id AND u.role.id!=1")
	public List<User> findByUserId(@Param("id") Long id);

	public User findByAssociateId(String employeeAssociateId);

	public User findByFirstname(String employeeFirstname);

	public User findByEmail(String employeeEmail);

	public Optional<User> getByAssociateId(String associateId);

	public Optional<User> findByUan(String employeeUAN);

	public Optional<User> findByBankAccountNumber(String employeeBankAccountNumber);


	

}
