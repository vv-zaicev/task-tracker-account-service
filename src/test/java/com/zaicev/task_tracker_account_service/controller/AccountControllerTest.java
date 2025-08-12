package com.zaicev.task_tracker_account_service.controller;

import com.zaicev.task_tracker_account_service.dto.*;
import com.zaicev.task_tracker_account_service.mapper.AccountMapper;
import com.zaicev.task_tracker_account_service.model.Account;
import com.zaicev.task_tracker_account_service.model.NotificationType;
import com.zaicev.task_tracker_account_service.service.AccountService;
import com.zaicev.task_tracker_account_service.service.AvatarService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountMapper accountMapper;

    @MockitoBean
    private AvatarService avatarService;


    @Test
    void getMe_ShouldReturnOKWithCorrectBody() throws Exception {
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

        when(accountService.getAccountById(1L)).thenReturn(account);
        when(accountMapper.toAccountResponse(account)).thenReturn(accountResponse);

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
    void getMe_WhenAccountNotFound_ShouldReturnNotFound() throws Exception {
        Exception exception = new EntityNotFoundException("Account not found");
        when(accountService.getAccountById(1L)).thenThrow(exception);

        mockMvc.perform(get("/account/me")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getMeNames_ShouldReturnOK() throws Exception {
        Account account = new Account();
        NamesResponse namesResponse = new NamesResponse("firstName", "lastName");

        when(accountService.getAccountById(1L)).thenReturn(account);
        when(accountMapper.toNamesResponse(account)).thenReturn(namesResponse);

        mockMvc.perform(get("/account/me/names")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(namesResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(namesResponse.getLastName()));

        verify(accountService).getAccountById(1L);
        verify(accountMapper).toNamesResponse(account);
    }

    @Test
    void getMyAvatar_WhenAvatarNotFound_ShouldReturnNotFound() throws Exception {
        Exception exception = new EntityNotFoundException("Account not found");
        when(avatarService.getAvatar(1L)).thenThrow(exception);

        mockMvc.perform(get("/account/me/avatar")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    void getMyAvatar_ShouldReturnAvatarWithCorrectHeaders() throws Exception {
        Long userId = 42L;
        byte[] imageBytes = "fakeImage".getBytes();
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        when(avatarService.getAvatar(userId)).thenReturn(resource);

        mockMvc.perform(get("/account/me/avatar")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", userId))))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=avatar42.jpg"))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    void getMeNames_WhenAccountNotFound_ShouldReturnNotFound() throws Exception {
        Exception exception = new EntityNotFoundException("Account not found");
        when(accountService.getAccountById(1L)).thenThrow(exception);

        mockMvc.perform(get("/account/me/names")
                        .with(jwt().jwt(jwt -> jwt.claim("userId", "1"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    void uploadMyAvatar_ShouldCallServiceAndReturn201() throws Exception {
        Long userId = 42L;
        MockMultipartFile file = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "fakeImage".getBytes()
        );

        // when / then
        mockMvc.perform(multipart("/account/me/avatar")
                        .file(file)
                        .with(jwt().jwt(jwt -> jwt.claim("userId", userId))))
                .andExpect(status().isCreated());

        ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        verify(avatarService).saveAvatar(ArgumentMatchers.eq(userId), fileCaptor.capture());
    }

    @Test
    void getAccountsForNotification_ShouldReturnOK() throws Exception {
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

        when(accountService.getAccountsForNotification(0)).thenReturn(accountPage);
        when(accountMapper.toAccountForNotificationPageResponse(accountPage)).thenReturn(pageResponse);

        mockMvc.perform(get("/account/for-notifications")
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
