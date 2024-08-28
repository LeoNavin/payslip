package com.pay.payslip.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author Leo Navin
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	public static final Contact DEFAULT_CONTACT = new Contact("Leo", "", "Leonavin99@gmail.com");
	public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Pay Slip", "JWT", "1.0", "urn:tos",
			DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());
	private static final List<String> PRODUCES_AND_CONSUMES = Arrays.asList("application/json");

	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO)
				.produces(new HashSet<String>(PRODUCES_AND_CONSUMES))
				.consumes(new HashSet<String>(PRODUCES_AND_CONSUMES));
		docket.globalOperationParameters(
				Arrays.asList(new ParameterBuilder().name("Authorization").description("Authroization Token")
						.modelRef(new ModelRef("string")).parameterType("header").required(false).build()));
		return docket;
	}
}