package com.zaicev.task_tracker_account_service;

import org.springframework.boot.SpringApplication;

public class TestTaskTrackerAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TaskTrackerAccountServiceApplication::main).with(MyTestcontainersConfiguration.class).run(args);
	}

}
