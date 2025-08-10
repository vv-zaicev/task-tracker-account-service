package com.zaicev.task_tracker_account_service.dto;

import com.zaicev.task_tracker_account_service.model.NotificationType;
import lombok.Value;

/**
 * DTO for {@link com.zaicev.task_tracker_account_service.model.AccountSetting}
 */
@Value
public class AccountSettingResponse {
    boolean needNotification;
    NotificationType notificationType;
}