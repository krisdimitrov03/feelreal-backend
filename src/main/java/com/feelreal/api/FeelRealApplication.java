package com.feelreal.api;

import com.feelreal.api.model.User;
import com.feelreal.api.seed.DatabaseSetup;
import com.feelreal.api.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FeelRealApplication {

	@Autowired
	private DatabaseSetup databaseSetup;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FeelRealApplication.class, args);
		// add one simple user
		User vanko = new User();
		vanko.setUsername("vanko eins");
		context.getBean(UserService.class).addUser(vanko);
		System.out.println("User added: " + vanko.getUsername());
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		createDatabaseAndTable();
	}

	private void createDatabaseAndTable() {
		databaseSetup.createDatabaseAndTable();
	}
}
