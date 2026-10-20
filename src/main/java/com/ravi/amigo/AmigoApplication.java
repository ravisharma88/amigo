package com.ravi.amigo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AmigoApplication {

	static void main(String[] args) {
		SpringApplication.run(AmigoApplication.class, args);
	}

}
