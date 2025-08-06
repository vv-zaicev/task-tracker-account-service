package com.zaicev.task_tracker_user_sevice.repository;

import com.zaicev.task_tracker_user_sevice.model.Account;
import com.zaicev.task_tracker_user_sevice.model.AccountSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindAllAccountWithNeedNotification() {
        // given
        AccountSetting setting1 = new AccountSetting();
        setting1.setNeedNotification(true);

        AccountSetting setting2 = new AccountSetting();
        setting2.setNeedNotification(false);

        Account account1 = Account.builder()
                .email("test1@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        Account account2 = Account.builder()
                .email("testAccount2@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .build();

        setting1 = entityManager.persistAndFlush(setting1);
        setting2 = entityManager.persistAndFlush(setting2);
        account1.setAccountSetting(setting1);
        account2.setAccountSetting(setting2);
        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Account> result = accountRepository.findAllAccountWithNeedNotification(pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail().equals("test1@example.com"));
        assertThat(result.getContent().get(0).getAccountSetting().isNeedNotification()).isTrue();
    }
}