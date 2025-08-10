package com.zaicev.task_tracker_account_service.controller;

import com.zaicev.task_tracker_account_service.dto.*;
import com.zaicev.task_tracker_account_service.mapper.AccountMapper;
import com.zaicev.task_tracker_account_service.model.Account;
import com.zaicev.task_tracker_account_service.model.NotificationType;
import com.zaicev.task_tracker_account_service.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountMapper accountMapper;


    @Test
    void getMe_success() throws Exception {
        Account account = new Account();
        AccountResponse accountResponse = AccountResponse.builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstName")
                .email("testEmail")
                .emailVerified(true)
                .phoneNumber("phoneNumber")
                .phoneNumberVerified(false)
                .accountSetting(new AccountSettingResponse(true, NotificationType.EMAIL))
                .build();

        Mockito.when(accountService.getAccountById(1L)).thenReturn(account);
        Mockito.when(accountMapper.toAccountResponse(account)).thenReturn(accountResponse);

        mockMvc.perform(get("/account/me")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountResponse.getId()))
                .andExpect(jsonPath("$.firstName").value(accountResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(accountResponse.getLastName()))
                .andExpect(jsonPath("$.email").value(accountResponse.getEmail()))
                .andExpect(jsonPath("$.emailVerified").value(accountResponse.isEmailVerified()))
                .andExpect(jsonPath("$.phoneNumber").value(accountResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.phoneNumberVerified").value(accountResponse.isPhoneNumberVerified()))
                .andExpect(jsonPath("$.accountSetting").hasJsonPath())
                .andExpect(jsonPath("$.accountSetting.needNotification").value(accountResponse.getAccountSetting().isNeedNotification()))
                .andExpect(jsonPath("$.accountSetting.notificationType").value(accountResponse.getAccountSetting().getNotificationType().toString()));
    }

    @Test
    void getMe_WhenAccountNotFound_ReturnNotFound() throws Exception{
        Exception exception = new EntityNotFoundException("Account not found");
        Mockito.when(accountService.getAccountById(1L)).thenThrow(exception);

        mockMvc.perform(get("/account/me")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getMeNames_success() throws Exception {
        Account account = new Account();
        NamesResponse namesResponse = new NamesResponse("firstName", "lastName");

        Mockito.when(accountService.getAccountById(1L)).thenReturn(account);
        Mockito.when(accountMapper.toNamesResponse(account)).thenReturn(namesResponse);

        mockMvc.perform(get("/account/me/names")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(namesResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(namesResponse.getLastName()));

        Mockito.verify(accountService).getAccountById(1L);
        Mockito.verify(accountMapper).toNamesResponse(account);
    }

    @Test
    void getMeNames_WhenAccountNotFound_ReturnNotFound() throws Exception{
        Exception exception = new EntityNotFoundException("Account not found");
        Mockito.when(accountService.getAccountById(1L)).thenThrow(exception);

        mockMvc.perform(get("/account/me/names")
                .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    void getAccountsForNotification_success() throws Exception {
        Account account = new Account();
        List<Account> accounts = List.of(account);
        Page<Account> accountPage = new PageImpl<>(accounts);
        AccountForNotificationResponse notificationResponse = AccountForNotificationResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .accountSettingNotificationType(NotificationType.EMAIL)
                .build();
        AccountForNotificationPageResponse pageResponse = new AccountForNotificationPageResponse(
                List.of(notificationResponse),
                false // no next page
        );

        Mockito.when(accountService.getAccountsForNotification(0)).thenReturn(accountPage);
        Mockito.when(accountMapper.toAccountForNotificationPageResponse(accountPage)).thenReturn(pageResponse);

        mockMvc.perform(get("/account")
                        .param("page", "0")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountForNotificationResponses.length()").value(1))
                .andExpect(jsonPath("$.accountForNotificationResponses[0].id").value(notificationResponse.getId()))
                .andExpect(jsonPath("$.accountForNotificationResponses[0].firstName").value(notificationResponse.getFirstName()))
                .andExpect(jsonPath("$.accountForNotificationResponses[0].lastName").value(notificationResponse.getLastName()))
                .andExpect(jsonPath("$.accountForNotificationResponses[0].email").value(notificationResponse.getEmail()))
                .andExpect(jsonPath("$.accountForNotificationResponses[0].phoneNumber").value(notificationResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.hasNext").value(pageResponse.isHasNext()))
                .andExpect(jsonPath("$.accountForNotificationResponses[0].accountSettingNotificationType").value(notificationResponse.getAccountSettingNotificationType().toString()));
    }
}
