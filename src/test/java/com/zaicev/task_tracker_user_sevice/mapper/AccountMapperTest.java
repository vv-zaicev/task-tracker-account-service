package com.zaicev.task_tracker_user_sevice.mapper;

import com.zaicev.task_tracker_user_sevice.dto.*;
import com.zaicev.task_tracker_user_sevice.model.Account;
import com.zaicev.task_tracker_user_sevice.model.AccountSetting;
import com.zaicev.task_tracker_user_sevice.model.NotificationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountMapperTest {

    @Mock
    private AccountSettingMapper accountSettingMapper;

    @InjectMocks
    private AccountMapper accountMapper;

    private final AccountSetting accountSetting = new AccountSetting(1L, true, NotificationType.EMAIL);


    private final AccountSettingResponse expectedAccountSettingResponse = new AccountSettingResponse(true, NotificationType.EMAIL);

    private final LocalDateTime  now = LocalDateTime.now();

    private final Account account = Account.builder()
            .id(1L)
            .firstName("firstName")
            .lastName("lastName")
            .email("email")
            .emailVerified(true)
            .phoneNumber("phoneNumber")
            .phoneNumberVerified(false)
            .accountSetting(accountSetting)
            .build();

    @Test
    void toAccountResponse_ShouldMapAccountToAccountResponse() {
        when(accountSettingMapper.toAccountSettingReponse(accountSetting)).thenReturn(expectedAccountSettingResponse);

        AccountResponse result = accountMapper.toAccountResponse(account);

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getFirstName(), result.getFirstName());
        assertEquals(account.getLastName(), result.getLastName());
        assertEquals(account.getEmail(), result.getEmail());
        assertEquals(account.isEmailVerified(), result.isEmailVerified());
        assertEquals(account.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(account.isPhoneNumberVerified(), result.isPhoneNumberVerified());
        assertEquals(expectedAccountSettingResponse, result.getAccountSetting());
    }

    @Test
    void toNamesResponse_ShouldMapAccountToNamesResponse() {
        NamesResponse result = accountMapper.toNamesResponse(account);

        assertNotNull(result);
        assertEquals(account.getFirstName(), result.getFirstName());
        assertEquals(account.getLastName(), result.getLastName());
    }

    @Test
    void toAccountForNotificationResponse_ShouldMapAccountToAccountForNotificationResponse() {
        AccountForNotificationResponse result = accountMapper.toAccountForNotificationResponse(account);


        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getFirstName(), result.getFirstName());
        assertEquals(account.getLastName(), result.getLastName());
        assertEquals(account.getAccountSetting().getNotificationType(), result.getAccountSettingNotificationType());
        assertEquals(account.getEmail(), result.getEmail());
        assertEquals(account.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void toAccountForNotificationPageResponse_ShouldMapPageToAccountForNotificationPageResponse() {
        Account account2 = Account.builder()
                .id(2L)
                .firstName("firstName2")
                .lastName("lastName2")
                .email("email2")
                .emailVerified(false)
                .phoneNumber("phoneNumber2")
                .phoneNumberVerified(true)
                .accountSetting(accountSetting)
                .createdAt(now.plusDays(1))
                .build();

        List<Account> accountList = List.of(account, account2);
        Page<Account> accountPage = new PageImpl<>(accountList);

        AccountForNotificationPageResponse result = accountMapper.toAccountForNotificationPageResponse(accountPage);

        assertNotNull(result);
        assertTrue(!result.isHasNext());
        assertEquals(2, result.getAccountForNotificationResponses().size());

        AccountForNotificationResponse firstAccount = result.getAccountForNotificationResponses().get(0);
        assertEquals(account.getId(), firstAccount.getId());
        assertEquals(account.getFirstName(), firstAccount.getFirstName());
        assertEquals(account.getLastName(), firstAccount.getLastName());
        assertEquals(account.getEmail(), firstAccount.getEmail());
        assertEquals(account.getPhoneNumber(), firstAccount.getPhoneNumber());

        AccountForNotificationResponse secondAccount = result.getAccountForNotificationResponses().get(1);
        assertEquals(account2.getId(), secondAccount.getId());
        assertEquals(account2.getFirstName(), secondAccount.getFirstName());
        assertEquals(account2.getLastName(), secondAccount.getLastName());
        assertEquals(account2.getEmail(), secondAccount.getEmail());
        assertEquals(account2.getPhoneNumber(), secondAccount.getPhoneNumber());
    }
}
