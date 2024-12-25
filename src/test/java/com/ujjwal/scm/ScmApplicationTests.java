package com.ujjwal.scm;

import com.ujjwal.scm.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScmApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService service;

	@Test
	void sendEmailTest(){

		service.sendEmail(
				"ujjwaldeeprock123@gmail.com",
				"Just testing email service",
				"this is scm project working on email service");
	}

}
