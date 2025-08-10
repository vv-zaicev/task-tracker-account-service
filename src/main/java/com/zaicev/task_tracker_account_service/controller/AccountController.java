package com.zaicev.task_tracker_account_service.controller;

import com.zaicev.task_tracker_account_service.dto.AccountForNotificationPageResponse;
import com.zaicev.task_tracker_account_service.dto.AccountResponse;
import com.zaicev.task_tracker_account_service.dto.ErrorResponse;
import com.zaicev.task_tracker_account_service.dto.NamesResponse;
import com.zaicev.task_tracker_account_service.mapper.AccountMapper;
import com.zaicev.task_tracker_account_service.model.Account;
import com.zaicev.task_tracker_account_service.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @GetMapping("/me")
    public AccountResponse getMe(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        return accountMapper.toAccountResponse(accountService.getAccountById(Long.valueOf(userId)));
    }

    @GetMapping("/me/names")
    public NamesResponse getMeNames(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        return accountMapper.toNamesResponse(accountService.getAccountById(Long.valueOf(userId)));
    }

    @GetMapping()
    public AccountForNotificationPageResponse getAccountsForNotification(@RequestParam(defaultValue = "0") int page){
        Page<Account> accountPage = accountService.getAccountsForNotification(page);
        return accountMapper.toAccountForNotificationPageResponse(accountPage);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountNotFound(EntityNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), Instant.now());
    }
}
