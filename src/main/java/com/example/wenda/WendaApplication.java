package com.example.wenda;

import com.example.wenda.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WendaApplication {
	User user = new User("xx");

	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}
}
