package com.pay.payslip.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pay.payslip.configuration.BcryptPasswordEncoder;
import com.pay.payslip.dto.ChangePasswordDto;
import com.pay.payslip.dto.LoginDto;
import com.pay.payslip.exception.ExceptionBean;
import com.pay.payslip.model.Month;
import com.pay.payslip.model.PaySlip;
import com.pay.payslip.model.Role;
import com.pay.payslip.model.User;
import com.pay.payslip.servie.MonthService;
import com.pay.payslip.servie.PaySlipService;
import com.pay.payslip.servie.RoleService;
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
public class UserController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private PaySlipService paySlipService;

	@Autowired
	private MonthService monthService;

	private ResponseModel response = new ResponseModel();

	private MappingJacksonValue mappingJacksonValue;

	private BcryptPasswordEncoder bcryptPasswordEncoder = new BcryptPasswordEncoder();

	@PostMapping("/login")
	public void validateUser(@RequestBody LoginDto user) {

	}

	@GetMapping("/otpgenerate")
	public ResponseEntity<Object> otpgenerate(@RequestHeader("Authorization") String token) {
		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {
			String message = null;
			UserController sendMail = new UserController();

//			List<String> email = new ArrayList<>(Arrays.asList(logInUser.get().getEmail(), "leonavin99@gmail.com")); 

			message = sendMail.sendEmail(logInUser.get().getEmail());
//			message = sendMail.sendEmail("leonavin99@gmail.com");
//			message=sendMail.sendEmail("edwin@boscosofttech.com");

			System.out.println(message);
			return responseCreation(true, "Success", "OTP Sent Successfully", message);

		}
		return responseCreation(false, "Failed", "Login User is Null", null, null);

	}

	public String sendEmail(String email) {
		final String username = "leonavin999@gmail.com";
		final String password = "trpdaffzphompczj";

//		final String username = "boscosoft.web5@gmail.com";
//		final String password = "oelztneusnsselll";

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
			int RANDOM_STRING_LENGTH = 6;
			StringBuffer randStr = new StringBuffer();
			for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
				int number = getRandomNumber();
				char ch = ConstantUtil.CHAR_LIST.charAt(number);
				randStr.append(ch);
			}

//			Random rand = new Random();
//			int otp=rand.nextInt(10000);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("leonavin999@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("OTP");
//			message.setText("Dear Sir/Madam," + "\n\n Please Enter the OTP \n\n"+randStr.toString());
//			String sourceString = "<b>" + id + "</b> " + name; 

			MimeMultipart multipart = new MimeMultipart();

			// first part (the html)
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			final String text = "Your OTP is : " + "<b>" + randStr.toString() + ".</b> ";
//			messageBodyPart.setContent("<b>" + randStr.toString() + "</b> ", "text/html");
			messageBodyPart.setContent(text, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

//			message.setText(""+randStr.toString()+"");

			Transport.send(message);

			String s = String.valueOf(randStr.toString());// Now it will return "10"
			return s;

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(ConstantUtil.CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	@PostMapping("save")
	public ResponseEntity<Object> saveUserData(@RequestHeader("Authorization") String token, @RequestBody User user) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			Optional<User> associateId = userService.findByAssociateId(user.getAssociateId());

			if (associateId.isEmpty()) {
				if (user != null && user.getAssociateId() != null) {

					if (user.getFirstname() != null) {

						Long userRole = (long) 2;

						Optional<Role> role = roleService.getById(userRole);

						if (user.getId() != null) {

							User user1 = userService.getByEmail(user.getEmail());

							if (user1 == null) {
								user.setPermissions("ADD,EDIT,SAVE,UPDATE");
								user.setStatus(true);
								user.setRole(role.get());

								System.out.println(user);
								user.setLastModifiedBy("ADMIN");
								SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								user.setLastModifiedDate(sdf1.format(new Date()));
								userService.updateUser(user);

								return responseCreation(true, "Success", "User is successfully Updated", null, user);
							}
							return responseCreation(false, "Failed", "User E-mail already exist", null, null);

						} else {

							User userEMail = userService.getByEmail(user.getEmail());
							Optional<User> userUan;
//									if(user.getUan()!= null) {
//										userUan=  userService.findByUAN(user.getUan());
//									}
							userUan = user.getUan() != null ? userService.findByUAN(user.getUan())
									: Optional.ofNullable(null);

							Optional<User> userBankAccountNumber;

							userBankAccountNumber = user.getBankAccountNumber() != null
									? userService.findByBankAccountNumber(user.getBankAccountNumber())
									: Optional.ofNullable(null);

//								userBankAccountNumber= userService
//										.findByBankAccountNumber(user.getBankAccountNumber());

							if (userEMail == null) {
								if (userUan.isEmpty()) {
									if (userBankAccountNumber.isEmpty()) {
										User newUser = new User(user.getAssociateId(), user.getFirstname(),
												user.getDateOfBirth(), user.getEmail(), user.getPhoneNumber(),
												bcryptPasswordEncoder.encode(user.getPassword()), user.getUan(),
												user.getDesignation(), user.getGender(), user.getUnit(), user.getDoj(),
												user.getBankName(), user.getBankAccountNumber(), true,
												"ADD,EDIT,SAVE,UPDATE", role.get());
										newUser.setProfilePath("");
										newUser.setUsername(user.getAssociateId());
										newUser.setCreatedBy("ADMIN");
										SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
										newUser.setCreatedDate(sdf1.format(new Date()));
										newUser = userService.saveUser(newUser);

										return responseCreation(true, "Success", "User is saved successfully", null,
												newUser);
									} else {
										return responseCreation(false, "Failed", "User BankAccountNumber already exist",
												null, null);
									}
								} else {
									return responseCreation(false, "Failed", "User Uan already exist", null, null);
								}
							}
							return responseCreation(false, "Failed", "User E-mail already exist", null, null);

						}
//								else {
//								return responseCreation(false, "Failed", "User Email & User Password Check Here!", null,
//										null);
//							}

					} else {
						return responseCreation(false, "Failed", "FirstName is Null !", null, null);
					}

				}
				return responseCreation(false, "Failed", "Admin username and password Faild ", null, null);
			}

			return responseCreation(false, "Failed", "associateId already exit ", null, null);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

//	@PostMapping("changeProfile")
	@PostMapping(value = "changeProfile", consumes = { "multipart/form-data" })
	public ResponseEntity<Object> changeProfile(@RequestHeader("Authorization") String token,
			@RequestParam("file") MultipartFile file) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {
			String filePath = null;
			if (file != null) {
//				User user=new User();
				String exitProfileName = null;
				exitProfileName = logInUser.get().getProfilePath();
				System.out.println("Old Profile Path  =" + exitProfileName);
				System.out.println(file.getSize());
				
				filePath = createProfile(file, logInUser);
				System.out.println("filePath" + filePath);
				logInUser.get().setProfilePath(filePath);
				System.out.println(logInUser.get().getProfilePath());
				logInUser.get().setLastModifiedBy(logInUser.get().getUsername());
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				logInUser.get().setLastModifiedDate(sdf1.format(new Date()));
				userService.updateUser(logInUser.get());
				System.out.println("New Profile Path  =" + logInUser.get().getProfilePath());
				System.out.println(logInUser.get().getProfilePath());

				if (exitProfileName != null) {
					String url = exitProfileName;
					String[] parts = url.split("/");
					String filename = parts[parts.length - 1];
					System.out.println(filename); // Outputs "Payslip_2023014_1137.jpg"
					if (new File(ConstantUtil.PAYSLIP_User_Profile_CONTEXT_PATH + filename).exists()) {
						new File(ConstantUtil.PAYSLIP_User_Profile_CONTEXT_PATH + filename).delete();
						System.out.println("File is deleted  =" +true);
					} 
				} 

			}
			return responseCreation(true, "Success", "User is saved successfully", filePath);
		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);
	}

	// Here this Method using for user Profile Image Writing path
	private String createProfile(MultipartFile file, Optional<User> logInUser) {
		String path = null;
		if (file != null) {
			Random random = new Random();
			String[] fileFrags = file.getOriginalFilename().split("\\.");
			String extension = fileFrags[fileFrags.length - 1];
			String fileName = "Payslip_" + logInUser.get().getUsername().replaceAll("\\s+", "_") + "_"
					+ Integer.toString(random.nextInt(10000)) + "." + extension;
			String filePath = ConstantUtil.PAYSLIP_User_Profile_CONTEXT_PATH + fileName;
//			String filePath = "D:\\jar\\test\\image\\" + fileName;
			File dest = new File(filePath);
			if (!new File(ConstantUtil.PAYSLIP_User_Profile_CONTEXT_PATH).exists()) {
				new File(ConstantUtil.PAYSLIP_User_Profile_CONTEXT_PATH).mkdir();
			}
			try {
				file.transferTo(dest);
			} catch (IllegalStateException | MultipartException | IOException e) {
				e.printStackTrace();
			}
			path = ConstantUtil.PAYSLIP_User_Profile_REPORT_PATH + fileName;
		}
		return path;
	}

	@PostMapping("/profileUpdate")
	public ResponseEntity<Object> profileUpdate(@RequestHeader("Authorization") String token, @RequestBody User user) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> loginUser = userService.findByUsername(jwt.getSubject());

		if (loginUser.get().getRole().getRole().equals("EMPLOYEE")) {

			Optional<User> employeeUser = userService.findByAssociateId(loginUser.get().getAssociateId());

			if (employeeUser.get().getId() != null) {

				Long userRole = (long) 2;

				Optional<Role> role = roleService.getById(userRole);

				if (user.getFirstname() != null) {
					employeeUser.get().setFirstname(user.getFirstname());
				}
//				if (user.getLastname() != null) {
//					employeeUser.get().setLastname(user.getLastname());
//				}
				if (user.getDateOfBirth() != null) {
					employeeUser.get().setDateOfBirth(user.getDateOfBirth());
				}
				if (user.getEmail() != null) {
					employeeUser.get().setEmail(user.getEmail());
				}

				if (user.getPhoneNumber() != null) {
					employeeUser.get().setPhoneNumber(user.getPhoneNumber());
				}
				if (user.getBankAccountNumber() != null) {
					employeeUser.get().setBankAccountNumber(user.getBankAccountNumber());
				}
				if (user.getBankName() != null) {
					employeeUser.get().setBankName(user.getBankName());
				}
				if (user.getDesignation() != null) {
					employeeUser.get().setDesignation(user.getDesignation());
				}
				if (user.getDoj() != null) {
					employeeUser.get().setDoj(user.getDoj());
				}
				if (user.getGender() != null) {
					employeeUser.get().setGender(user.getGender());
				}
				if (user.getUan() != null) {
					employeeUser.get().setUan(user.getUan());
				}
				if (user.getUnit() != null) {
					employeeUser.get().setUnit(user.getUnit());
				}
				if (user.getUsername() != null) {
					employeeUser.get().setUsername(user.getAssociateId());
				}
				if (user.getPassword() != null) {
					employeeUser.get().setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
				}
				if (user.getProfilePath() == null) {
					employeeUser.get().setProfilePath("");
				} else {
					employeeUser.get().setProfilePath(user.getProfilePath());
				}

				employeeUser.get().setPermissions("ADD,EDIT,SAVE,UPDATE");
				employeeUser.get().setStatus(true);
				employeeUser.get().setRole(role.get());
//				userService.updateUser(employeeUser.get());

				User userEmail = userService.getByEmail(user.getEmail());

				Optional<User> userUan = userService.findByUAN(user.getUan());

				Optional<User> userBankAccountNumber = userService.findByBankAccountNumber(user.getBankAccountNumber());

				ArrayList<String> errorList = new ArrayList<String>();

				if (userEmail != null) {
					if (!userEmail.getAssociateId().equals(user.getAssociateId())) {
						errorList.add("E-Mail is Already exist");
					}
				}
				if (userUan.isPresent()) {
					if (!userUan.get().getAssociateId().equals(user.getAssociateId())) {
						errorList.add("UAN is Already exist");
					}
				}

				if (userBankAccountNumber.isPresent()) {
					if (!userBankAccountNumber.get().getAssociateId().equals(user.getAssociateId())) {
						errorList.add("BankAccountNumber is Already exist");
					}
				}

				if (errorList.size() == 0) {
					userService.updateUser(employeeUser.get());
					return responseCreation(true, "Success", "User is Update successfully", null, null);
				} else {
					return responseUserUpadteCreation(false, "Failed", null, null, errorList);
				}

			} else {
				return responseCreation(false, "Failed", "Employee Id can't be null !", null, null);
			}

		} else if (loginUser.get().getRole().getRole().equals("ADMIN")) {

			Optional<User> adminUser;
			Long userRole;

//			if (loginUser.get().getAssociateId() == user.getAssociateId()) {
//				adminUser = userService.findByAssociateId(loginUser.get().getAssociateId());
//				userRole = (long) 1;
//			} else {
//				adminUser = userService.findByAssociateId(user.getAssociateId());
//				userRole = (long) 2;
//			}

			if (loginUser.get().getAssociateId().equals(user.getAssociateId())) {
				adminUser = userService.findByAssociateId(loginUser.get().getAssociateId());
				userRole = (long) 1;
			} else {
				adminUser = userService.findByAssociateId(user.getAssociateId());
				userRole = (long) 2;
			}

			if (adminUser.get().getUsername() != null) {

				Optional<Role> role = roleService.getById(userRole);

				if (adminUser.get().getId() != null) {

					if (user.getFirstname() != null) {
						adminUser.get().setFirstname(user.getFirstname());
					}
//					if (user.getLastname() != null) {
//						adminUser.get().setLastname(user.getLastname());
//					}
					if (user.getDateOfBirth() != null) {
						adminUser.get().setDateOfBirth(user.getDateOfBirth());
					}
					if (user.getEmail() != null) {
						adminUser.get().setEmail(user.getEmail());
					}

					if (user.getPhoneNumber() != null) {
						adminUser.get().setPhoneNumber(user.getPhoneNumber());
					}
					if (user.getBankAccountNumber() != null) {
						adminUser.get().setBankAccountNumber(user.getBankAccountNumber());
					}
					if (user.getBankName() != null) {
						adminUser.get().setBankName(user.getBankName());
					}
					if (user.getDesignation() != null) {
						adminUser.get().setDesignation(user.getDesignation());
					}
					if (user.getDoj() != null) {
						adminUser.get().setDoj(user.getDoj());
					}
					if (user.getGender() != null) {
						adminUser.get().setGender(user.getGender());
					}
					if (user.getUan() != null) {
						adminUser.get().setUan(user.getUan());
					}
					if (user.getUnit() != null) {
						adminUser.get().setUnit(user.getUnit());
					}
					if (user.getUsername() != null) {
						// here setting AssociateId to Username Same Values
						adminUser.get().setUsername(user.getAssociateId());
					}
					if (user.getPassword() != null) {
						adminUser.get().setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
					}
					if (user.getProfilePath() == null) {
						adminUser.get().setProfilePath("");
					} else {
						adminUser.get().setProfilePath(user.getProfilePath());
					}

					adminUser.get().setPermissions("ADD,EDIT,SAVE,UPDATE");
					adminUser.get().setStatus(true);
					adminUser.get().setRole(role.get());

					adminUser.get().setLastModifiedBy("ADMIN");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					adminUser.get().setLastModifiedDate(sdf1.format(new Date()));

					ArrayList<String> errorList = new ArrayList<String>();

					if (user.getEmail() != null) {
						User userEmail = userService.getByEmail(user.getEmail());
						if (userEmail != null) {
							if (!userEmail.getAssociateId().equals(user.getAssociateId())) {
								errorList.add("E-Mail is Already exist");
							}
						}
					}

					if (user.getUan() != null) {
						Optional<User> userUan = userService.findByUAN(user.getUan());
						if (userUan.isPresent()) {
							if (!userUan.get().getAssociateId().equals(user.getAssociateId())) {
								errorList.add("UAN is Already exist");
							}
						}
					}
					if (user.getBankAccountNumber() != null) {
						Optional<User> userBankAccountNumber = userService
								.findByBankAccountNumber(user.getBankAccountNumber());
						if (userBankAccountNumber.isPresent()) {
							if (!userBankAccountNumber.get().getAssociateId().equals(user.getAssociateId())) {
								errorList.add("BankAccountNumber is Already exist");
							}
						}
					}

					if (errorList.size() == 0) {
						userService.updateUser(adminUser.get());
						return responseCreation(true, "Success", "User is Update successfully", null, null);
					} else {
						return responseUserUpadteCreation(false, "Failed", null, null, errorList);
					}

				} else {
					return responseCreation(false, "Failed", "Admin Id can't be null", null, null);
				}
			} else {
				return responseCreation(false, "Failed", "Username can't be null!", null, null);
			}
		}

		return responseCreation(false, "Failed", "Admin role can be null.", null, null);

	}

	@GetMapping("/getUser")
	public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String token) throws ParseException {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {

			Optional<User> user = userService.findByUserId(logInUser.get().getId());

//			String ds1 =user.get().getDoj();
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
//			String ds2 = sdf2.format(sdf1.parse(ds1));		
//			user.get().setDoj(ds2);
			return responseCreation(true, "Success", "Get User details", null, user.get());

		}

		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@GetMapping("/getUserById")
	public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String token, @RequestParam("id") Long id) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			Optional<User> user = userService.findByUserId(id);

			return responseCreation(true, "Success", "Get User details", null, user.get());

		}

		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@GetMapping("/getAllUser")
	public ResponseEntity<Object> getAllUser(@RequestHeader("Authorization") String token) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

//			String role="EMPLOYEE";

			List<User> userList = userService.findByAlluser(logInUser.get().getId());
			boolean deleteStatus = true;
			for (User user : userList) {
				List<PaySlip> usermappingMonthandYear = paySlipService.findByAssociateId(user.getAssociateId());
				if (usermappingMonthandYear.size() > 0) {
					deleteStatus = false;
				} else {
					deleteStatus = true;
				}
				user.setDeleteStatus(deleteStatus);
			}
			System.out.println(userList);
			return responseCreation(true, "Success", "Get All User data", userList, null);

		} else {
			if (logInUser.isPresent()) {

				Optional<User> user = userService.findByUserId(logInUser.get().getId());

				if (user.isPresent()) {
					List<User> user1 = new ArrayList<User>();
					user1.add(user.get());

					return responseCreation(true, "Success", "Get All User data", user1, null);

				}
			}

		}

		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@DeleteMapping("/deleteByUserId")
	public ResponseEntity<Object> deleteByUser(@RequestHeader("Authorization") String token, @RequestParam Long id) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			userService.getByUserId(id);

			return responseCreation(true, "Success", "Delete User successfully", null, null);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@GetMapping("/getAllMonth")
	public ResponseEntity<Object> getAllMonth(@RequestHeader("Authorization") String token) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.get().getRole().getRole().equals("ADMIN")) {

			List<Month> month = monthService.findByAllMonth();

			return responseCreation(true, "Success", "Get All User data", month);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	@PostMapping("/changePassowrd")
	public ResponseEntity<Object> changePassowrd(@RequestHeader("Authorization") String token,
			@RequestBody ChangePasswordDto changePasswordDto) {

		String jwtToken = token.replaceFirst("Bearer", "");

		DecodedJWT jwt = JWT.decode(jwtToken.trim());

		Optional<User> logInUser = userService.findByUsername(jwt.getSubject());

		if (logInUser.isPresent()) {

//			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();  
//			if( bcrypt.matches(changePasswordDto.getConfirmPassword(), logInUser.get().getPassword())) {

//			if(logInUser.get().getPassword() == changePasswordDto.getOldPassword()) 
			if (bcryptPasswordEncoder.checkPassword(changePasswordDto.getOldPassword(),
					logInUser.get().getPassword())) {

				if (changePasswordDto.getConfirmPassword() != null) {
					logInUser.get().setPassword(bcryptPasswordEncoder.encode(changePasswordDto.getConfirmPassword()));
					userService.updateUser(logInUser.get());
					return responseCreation(true, "Success", "Password Successfully Updated", null, null);

				}

				return responseCreation(false, "Failed", "Confirm Password can't be empty", null, null);

			}

			return responseCreation(false, "Failed", "Old Password is wrong", null, null);

		}
		return responseCreation(false, "Failed", "User can't be null!", null, null);

	}

	private ResponseEntity<Object> responseCreation(boolean status, String message, String description,
			List<Month> month) {
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (month != null) {
			response.setMonth(month);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "month" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
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

	private ResponseEntity<Object> responseCreation(boolean status, String message, String description, String string) {
		// TODO Auto-generated method stub
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (string != null) {
			response.setOtp(string);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "otp" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

	private ResponseEntity<Object> responseUserUpadteCreation(boolean status, String message, String description,
			String filepath, List<String> string) {
		// TODO Auto-generated method stub
		response.setStatus(status);
		response.setInformation(new ExceptionBean(new Date(), message, description));
		if (filepath != null) {
			response.setFile(filepath);
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information", "file" });
		} else if (string != null) {
			response.setMessages(string);
			mappingJacksonValue = FilterUtil.filterFields(response,
					new String[] { "status", "information", "messages" });
		} else {
			mappingJacksonValue = FilterUtil.filterFields(response, new String[] { "status", "information" });
		}
		return new ResponseEntity<Object>(mappingJacksonValue, HttpStatus.OK);
	}

}
//user.getUsername(), bcryptPasswordEncoder.encode(user.getPassword()),
//user.getName(), user.getFirstname(), user.getLastname(), user.getEmail(),
//user.getContactNo(), user.getPlace(), user.getAddress(), true, "ADD,EDIT,SAVE,UPDATE",
//role.get()