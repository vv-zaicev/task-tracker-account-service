package com.zaicev.task_tracker_account_service.mapper;

import com.zaicev.task_tracker_account_service.dto.AccountSettingResponse;
import com.zaicev.task_tracker_account_service.model.AccountSetting;
import org.springframework.stereotype.Component;

@Component
public class AccountSettingMapper {
    public AccountSettingResponse toAccountSettingResponse(AccountSetting accountSetting) {
        return new AccountSettingResponse(accountSetting.isNeedNotification(), accountSetting.getNotificationType());
    }
}
