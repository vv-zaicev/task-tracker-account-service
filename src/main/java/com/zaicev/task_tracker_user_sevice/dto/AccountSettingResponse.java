package com.zaicev.task_tracker_user_sevice.dto;

import com.zaicev.task_tracker_user_sevice.model.NotificationType;
import lombok.Value;

/**
 * DTO for {@link com.zaicev.task_tracker_user_sevice.model.AccountSetting}
 */
@Value
public class AccountSettingResponse {
    boolean needNotification;
    NotificationType notificationType;
}