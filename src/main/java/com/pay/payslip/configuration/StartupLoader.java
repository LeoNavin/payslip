package com.pay.payslip.configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.pay.payslip.model.Month;
import com.pay.payslip.model.Role;
import com.pay.payslip.model.User;
import com.pay.payslip.repository.MonthRepositary;
import com.pay.payslip.repository.RoleRepository;
import com.pay.payslip.repository.UserRepository;
/**
 * @author Leo Navin
 *
 */
@Service
public class StartupLoader implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MonthRepositary monthRepositary;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

	@Override
	public void run(String... args) throws Exception {
		createRoleIfNotExist("ADMIN");
		createRoleIfNotExist("EMPLOYEE");

//		createMonthIfNotExist("JANUARY");(" February, March, April, May, June, July, August, September, October, November, and December");

		List<String> monthsList = new ArrayList<>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August",
				"September","October","November","December"));

//		ArrayList<String> monthsList=new ArrayList<>();
//		monthsList.add("January");
//		monthsList.add("February");
//		monthsList.add("March");
//		monthsList.add("April");
//		monthsList.add("May");
//		monthsList.add("June");
//		monthsList.add("July");
//		monthsList.add("August");
//		monthsList.add("September");
//		monthsList.add("October");
//		monthsList.add("November");
//		monthsList.add("December");
		
		createMonthIfNotExist(monthsList);

		createAdminUserIfNotExist("Admin");
//		createAdminUserIfNotExist1("Admin12");

	}


	private void createMonthIfNotExist(List<String> monthsList) {
//		System.out.println("dsfsdrg"+months);
		
		 for (String monthValue : monthsList) {
			 
			 String month=monthValue.toUpperCase();
			 
				Optional<Month> checkMonth = monthRepositary.findByMonth(month);
				
				if(!checkMonth.isPresent()){
					Month mon=new Month();
					mon.setMonth(month);
					mon.setCreatedBy("System");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					mon.setCreatedDate(sdf1.format(new Date()));
					monthRepositary.saveAndFlush(mon);
				}

			 

	        }
	}

//	private void createCustomerUserIfNotExist(String username) {
//		Optional<User> checkUser = userRepository.findByUsername(username);
//		if(!checkUser.isPresent()) {
//			User user = new User("Employee", "Employee", "Employee@gmail.com", "9047162016", "Employee",  bcryptPasswordEncoder.encode("Employee"),true, roleRepository.findByRole("Employee").get());
//			user.setPermissions("ADD,EDIT,DELETE,LIST");				
//			userRepository.saveAndFlush(user);		
//		}
//	}
	//edwin@boscosofttech.com

//	private void createAdminUserIfNotExist1(String username) {
//		Optional<User> checkUser = userRepository.findByUsername(username);
//		if (!checkUser.isPresent()) {
//			User user = new User("Admin", "Admin", "leonavin999@gmail.com", "9047162016", "Admin12",
//					bcryptPasswordEncoder.encode("Admin@1234"), true, roleRepository.findByRole("ADMIN").get());
//			user.setPermissions("ADD,EDIT,DELETE,LIST");
//			user.setAssociateId("2023001");
//			user.setProfilePath("");
//			user.setCreatedBy("System");
//			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//			user.setCreatedDate(sdf1.format(new Date()));
//			userRepository.saveAndFlush(user);
//
//		}
//	}
	private void createAdminUserIfNotExist(String username) {
		Optional<User> checkUser = userRepository.findByUsername(username);
		if (!checkUser.isPresent()) {
			User user = new User("Admin", "Admin", "leonavin99@gmail.com", "9047162016", "Admin",
					bcryptPasswordEncoder.encode("Admin@123"), true, roleRepository.findByRole("ADMIN").get());
			user.setPermissions("ADD,EDIT,DELETE,LIST");
			user.setAssociateId("2023001");
			user.setProfilePath("");
			user.setCreatedBy("System");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			user.setCreatedDate(sdf1.format(new Date()));
			userRepository.saveAndFlush(user);

		}
	}
	
	

	private void createRoleIfNotExist(String role) {
		Optional<Role> checkUserRole = roleRepository.findByRole(role);
		if (!checkUserRole.isPresent()) {
			Role role2 = new Role(role, role, true);
			role2.setCreatedBy("System");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			role2.setCreatedDate(sdf1.format(new Date()));
			roleRepository.saveAndFlush(role2);
		}
	}

}
