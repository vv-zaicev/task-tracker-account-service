package com.zaicev.task_tracker_user_sevice;

import org.springframework.boot.SpringApplication;

public class TestTaskTrackerUserSeviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TaskTrackerUserSeviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
