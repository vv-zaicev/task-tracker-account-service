package com.zaicev.task_tracker_user_sevice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@SpringBootTest
@Import(MyTestcontainersConfiguration.class)
class TaskTrackerUserSeviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
