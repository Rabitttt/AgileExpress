package com.obss.AgileExpress;

import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.entity.User;
import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.repository.UserRepository;
import com.obss.AgileExpress.service.ProjectService;
import com.obss.AgileExpress.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoAuditing
public class AgileExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgileExpressApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@Bean
	CommandLineRunner runner(ProjectService projectService) {
		User user = userRepository.findUserByEmail("john@gmail.com");
		return args -> {
			projectService.createProject(
					new Project(
							null,
							"Project 0",
							"This is project 1",
							null,
							null,
							user,
							user,
							List.of(user),
							List.of("deneme","heloloooo","task3"),
							List.of("status 1","status 2","status 3")
					));
		};
	}


	/*
	@Bean
	CommandLineRunner runner(UserService userService) {
		return args -> {
			userService.saveUser(new User(null,"John Travolta","john@gmail.com","Selman",new ArrayList<>(List.of(UserRoles.Admin.name()))));
			userService.saveUser(new User(null,"Will Smith","will@gmail.com","1234",new ArrayList<>(List.of(UserRoles.TeamMember.name()))));
			userService.saveUser(new User(null,"Jim Carry","jim@gmail.com","1234",new ArrayList<>(List.of(UserRoles.TeamLeader.name()))));
			userService.saveUser(new User(null,"Arnold Schwarzenegger","arnold@gmail.com","1234",(List.of(UserRoles.ProjectManager.name()))));
		};
	}
	 */
}
