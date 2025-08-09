package com.zaicev.task_tracker_user_sevice.repository;

import com.zaicev.task_tracker_user_sevice.model.Account;
import com.zaicev.task_tracker_user_sevice.model.AccountSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportTestcontainers(TestcontainersConfiguration.class)
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindAllAccountWithNeedNotification() {
        AccountSetting setting1 = new AccountSetting();
        setting1.setNeedNotification(true);

        AccountSetting setting2 = new AccountSetting();
        setting2.setNeedNotification(false);

        Account account1 = Account.builder()
                .email("test1@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .accountSetting(setting1)
                .build();
        Account account2 = Account.builder()
                .email("testAccount2@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .accountSetting(setting2)
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Account> result = accountRepository.findAllAccountWithNeedNotification(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(result.getContent().get(0).getAccountSetting().isNeedNotification()).isTrue();
    }
}