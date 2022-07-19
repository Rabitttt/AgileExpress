package com.obss.AgileExpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AgileExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgileExpressApplication.class, args);
	}
}
