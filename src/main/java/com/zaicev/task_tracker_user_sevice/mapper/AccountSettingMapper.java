package com.zaicev.task_tracker_user_sevice.mapper;

import com.zaicev.task_tracker_user_sevice.dto.AccountSettingResponse;
import com.zaicev.task_tracker_user_sevice.model.AccountSetting;
import org.springframework.stereotype.Component;

@Component
public class AccountSettingMapper {
    public AccountSettingResponse toAccountSettingResponse(AccountSetting accountSetting) {
        return new AccountSettingResponse(accountSetting.isNeedNotification(), accountSetting.getNotificationType());
    }
}
