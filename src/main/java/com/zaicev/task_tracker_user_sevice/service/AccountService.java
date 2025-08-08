package com.zaicev.task_tracker_user_sevice.service;

import com.zaicev.task_tracker_user_sevice.model.Account;
import com.zaicev.task_tracker_user_sevice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private static final int PAGE_SIZE = 100;

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account id = %d not found".formatted(id)));
    }

    public Page<Account> getAccountsForNotification(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return accountRepository.findAllAccountWithNeedNotification(pageable);
    }

}
