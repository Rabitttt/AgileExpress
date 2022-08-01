package com.obss.AgileExpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoAuditing
public class AgileExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgileExpressApplication.class, args);
	}

/*
	@Bean
	CommandLineRunner runner(SprintRepository sprintRepository, TaskRepository taskRepository) {
		Sprint sprint = sprintRepository.findById("62e04f554f8c800eadc78914").orElseThrow();
		Task task = taskRepository.findById("62dfea410dd3cb665a023726").orElseThrow();
		task.setStatus("sdfgsdfg");
		sprint.getTasks().add(task);
		return args -> {
			taskRepository.save(task);
			sprintRepository.save(sprint);
		};
	}
 */


/*
	@Bean
	CommandLineRunner runner(ProjectService projectService,UserRepository userRepository) {
		User creator = userRepository.findUserByEmail("john@gmail.com");
		User teamMember = userRepository.findUserByEmail("will@gmail.com");
		User teamLeader = userRepository.findUserByEmail("jim@gmail.com");
		User projectManager = userRepository.findUserByEmail("arnold@gmail.com");
		return args -> {
			projectService.createProject(
					new Project(
							null,
							"Project 0",
							"This is project 1",
							null,
							null,
							creator,
							teamLeader,
							projectManager,
							List.of(teamMember),
							List.of("deneme","heloloooo","task3"),
							List.of("status 1","status 2","status 3")
					));
		};
	}

 */


/*
	@Bean
	CommandLineRunner runner(UserService userService) {
		return args -> {
			userService.saveUser(new User(null,"John Travolta","john@gmail.com","Selman",new ArrayList<>(List.of(UserRoles.Admin.name()))));
			userService.saveUser(new User(null,"Will Smith","will@gmail.com","benspassword",new ArrayList<>(List.of(UserRoles.TeamMember.name()))));
			userService.saveUser(new User(null,"Great Gatsby","great@gmail.com","benspassword",(List.of(UserRoles.TeamMember.name()))));
			userService.saveUser(new User(null,"Manga","manga@gmail.com","benspassword",(List.of(UserRoles.TeamMember.name()))));
			userService.saveUser(new User(null,"Jim Carry","jim@gmail.com","benspassword",new ArrayList<>(List.of(UserRoles.TeamLeader.name()))));
			userService.saveUser(new User(null,"Lionel Messi","lionel@gmail.com","benspassword",(List.of(UserRoles.TeamLeader.name()))));
			userService.saveUser(new User(null,"Arnold Schwarzenegger","arnold@gmail.com","benspassword",(List.of(UserRoles.ProjectManager.name()))));
			userService.saveUser(new User(null,"Vito Corleone","vito@gmail.com","benspassword",(List.of(UserRoles.ProjectManager.name()))));

		};
	}
 */

}
