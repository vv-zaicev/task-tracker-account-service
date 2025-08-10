package com.zaicev.task_tracker_user_sevice;

import org.springframework.boot.SpringApplication;

public class TestTaskTrackerUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TaskTrackerUserSeviceApplication::main).with(MyTestcontainersConfiguration.class).run(args);
	}

}
