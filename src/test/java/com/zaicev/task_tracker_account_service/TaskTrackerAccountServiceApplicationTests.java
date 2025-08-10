package com.zaicev.task_tracker_account_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@SpringBootTest
@Import(MyTestcontainersConfiguration.class)
class TaskTrackerAccountServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
