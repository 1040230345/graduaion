package com.jackson;

import com.jackson.webSocket.controller.WebSocketController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GraduationApplication {

	public static void main(String[] args) {
//		SpringApplication.run(GraduationApplication.class, args);
		SpringApplication springApplication = new SpringApplication(GraduationApplication.class);

		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		//解决WebSocket不能注入的问题
		WebSocketController.setApplicationContext(configurableApplicationContext);
	}

}
