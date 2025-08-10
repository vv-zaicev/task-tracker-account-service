package com.zaicev.task_tracker_account_service.mapper;

import com.zaicev.task_tracker_account_service.dto.AccountForNotificationPageResponse;
import com.zaicev.task_tracker_account_service.dto.AccountForNotificationResponse;
import com.zaicev.task_tracker_account_service.dto.AccountResponse;
import com.zaicev.task_tracker_account_service.dto.NamesResponse;
import com.zaicev.task_tracker_account_service.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final AccountSettingMapper accountSettingMapper;

    public AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .emailVerified(account.isEmailVerified())
                .phoneNumber(account.getPhoneNumber())
                .phoneNumberVerified(account.isPhoneNumberVerified())
                .accountSetting(accountSettingMapper.toAccountSettingResponse(account.getAccountSetting()))
                .build();
    }

    public NamesResponse toNamesResponse(Account account) {
        return new NamesResponse(account.getFirstName(), account.getLastName());
    }

    public AccountForNotificationPageResponse toAccountForNotificationPageResponse(Page<Account> accountPage) {
        return new AccountForNotificationPageResponse(accountPage.getContent().stream().map(this::toAccountForNotificationResponse).toList(), accountPage.hasNext());
    }

    public AccountForNotificationResponse toAccountForNotificationResponse(Account account) {
        return AccountForNotificationResponse.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .accountSettingNotificationType(account.getAccountSetting().getNotificationType())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .build();
    }

}
