package com.zaicev.task_tracker_user_sevice.service;

import com.zaicev.task_tracker_user_sevice.model.Account;
import com.zaicev.task_tracker_user_sevice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
    }

    @Test
    void getUserById_WhenAccountExists_ShouldReturnAccount() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

        Account result = accountService.getAccountById(accountId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(testAccount.getId(), result.getId());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getUserById_WhenAccountNotExists_ShouldThrowEntityNotFoundException() {
        Long accountId = 999L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> accountService.getAccountById(accountId)
        );

        Assertions.assertTrue(exception.getMessage().contains("Account id = 999 not found"));
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccountsForNotification_ShouldReturnPageOfAccounts() {
        int page = 0;
        List<Account> accounts = List.of(testAccount);
        Page<Account> expectedPage = new PageImpl<>(accounts);

        when(accountRepository.findAllAccountWithNeedNotification(any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<Account> result = accountService.getAccountsForNotification(page);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(testAccount.getId(), result.getContent().get(0).getId());
        verify(accountRepository).findAllAccountWithNeedNotification(
                eq(PageRequest.of(page, 100))
        );
    }

    @Test
    void getAccountsForNotification_WhenPageIsEmpty_ShouldReturnEmptyPage() {
        int page = 5;
        Page<Account> expectedPage = new PageImpl<>(List.of());

        when(accountRepository.findAllAccountWithNeedNotification(any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<Account> result = accountService.getAccountsForNotification(page);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.getContent().size());
        verify(accountRepository).findAllAccountWithNeedNotification(
                eq(PageRequest.of(page, 100))
        );
    }
}
