package com.notification.hnguser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HnguserApplication {

	public static void main(String[] args) {
		SpringApplication.run(HnguserApplication.class, args);
	}

}
