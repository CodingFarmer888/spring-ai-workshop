package com.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

@Push
@SpringBootApplication
public class AiWorkshopApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(AiWorkshopApplication.class, args);
	}

}
