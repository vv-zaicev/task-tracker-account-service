package com.zaicev.task_tracker_user_sevice.dto;

import com.zaicev.task_tracker_user_sevice.model.NotificationType;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.zaicev.task_tracker_user_sevice.model.Account}
 */
@Value
@Builder
public class AccountForNotificationResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    NotificationType accountSettingNotificationType;
}