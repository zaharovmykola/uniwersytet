package com.zakharov.mykola.uniwersytet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class UniwersytetApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniwersytetApplication.class, args);
	}

}
