/**
 * 
 */
package com.pay.payslip.controller;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.dto.ForgetPasswordDto;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.User;
import com.pay.payslip.repository.UserRepository;
import com.pay.payslip.servie.UserService;
import com.pay.payslip.util.ConstantUtil;
import com.pay.payslip.util.FilterUtil;
import com.pay.payslip.util.ResponseModel;

/**
 * @author Leo Navin
 *
 */
@RestController
@RequestMapping(value = "/user")
public class ForgetPassword {

//	@Autowired
//	private RoleService roleService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	private ResponseModel response = new ResponseModel();

	private MappingJacksonValue mappingJacksonValue;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

//	@PostMapping("/forgotpassword")
	public ResponseEntity<Object> monthAndYearSave(@RequestBody ForgetPasswordDto forgetPasswordDto) {
		
		ForgetPassword sendMail = new ForgetPassword();
		
		User user = userService.findByAllUseremail(forgetPasswordDto.getEmail());

		sendMail.sendEmail(user);
//		sendMail.sendEmail(forgetPasswordDto.getEmail());

		return responseCreation(true, "Success", "User is saved successfully", null, null);

	}

	public void sendEmail(User user) {
		
		int RANDOM_STRING_LENGTH = 8;
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = ConstantUtil.CHAR_AND_NUMBER_LIST.charAt(number);
			randStr.append(ch);
		}
//		System.out.println(randStr.toString());
//		System.out.println(bcryptPasswordEncoder.encode(randStr.toString()));
		String password1=bcryptPasswordEncoder.encode(randStr.toString());
		user.setPassword(password1);
//		System.out.println(user);
		userRepository.saveAndFlush(user);
		

		final String username = "leonavin999@gmail.com";
		final String password = "trpdaffzphompczj";
//		final String username = "hr@boscoits.com";
//		final String password = "lujfrkkrtlgezeer";


		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("leonavin999@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
			message.setSubject("Change Password");
			MimeMultipart multipart = new MimeMultipart();

			// first part (the html)
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			final String text = "Successfuly changed your Password" + "<br><br>" + "<b>" + randStr.toString() + "</b> ";
			messageBodyPart.setContent(text, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(ConstantUtil.CHAR_AND_NUMBER_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	
	
	
	
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<Object> sendMailPassword(@RequestBody ForgetPasswordDto userForgetPassworod) {

		User user = userService.findByAllUseremail(userForgetPassworod.getEmail());

		if (user != null) {

			if (user.getEmail().equals(userForgetPassworod.getEmail())) {

				final String username = "leonavin999@gmail.com";
				final String password = "trpdaffzphompczj";
//				final String username = "hr@boscoits.com";
//				final String password = "lujfrkkrtlgezeer";


				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.ssl.protocols", "TLSv1.2");

				Session session = Session.getInstance(props, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("leonavin999@gmail.com"));
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
					message.setSubject("Change Password");


//					Random password1 = new Random();
//					int number = password1.nextInt(999999);
//					System.out.println(password1);

					int RANDOM_STRING_LENGTH = 8;
					StringBuffer randStr = new StringBuffer();
					for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
						int number = getRandomNumber();
						char ch = ConstantUtil.CHAR_AND_NUMBER_LIST.charAt(number);
						randStr.append(ch);
					}
					
					MimeMultipart multipart = new MimeMultipart();

					// first part (the html)
					MimeBodyPart messageBodyPart = new MimeBodyPart();
					final String text = "Successfuly changed your Password" + "<br><br>" + "<b>" + randStr.toString() + "</b> ";
					messageBodyPart.setContent(text, "text/html");

					// add it
					multipart.addBodyPart(messageBodyPart);
					message.setContent(multipart);
					
					

//					message.setText("Dear Sir/Madam," + "\n\n  Your Password will be changed below is given:\n\n"
//							+ randStr.toString());

//					user.setPassword(bcryptPasswordEncoder.encode(number + user.getUsername()));
//					userService.saveUser(user);
					
					
					user.setPassword(bcryptPasswordEncoder.encode(randStr.toString()));
//					System.out.println(user);
					userService.updateUser(user);

					Transport.send(message);

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
				return responseCreation(true, "Success", "User password send our mail ID please check", null, null);
			}
			return responseCreation(false, "Failed", "Email id can't match", null, null);
		}
		return responseCreation(false, "Failed", "Email id is Invalid", null, null);
	}
	
	
	
	
	
	private ResponseEntity<Object> responseCreation(boolean status, String message, String description,
			List<User> users, User user) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (users != null) {
			response.setUsers(users);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "users" });
		} else if (user != null) {
			response.setUser(user);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "user" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}
}
