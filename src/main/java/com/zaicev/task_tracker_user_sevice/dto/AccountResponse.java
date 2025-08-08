package com.zaicev.task_tracker_user_sevice.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.zaicev.task_tracker_user_sevice.model.Account}
 */
@Value
@Builder
public class AccountResponse {
    Long id;
    String username;
    String firstName;
    String lastName;
    String email;
    boolean emailVerified;
    String phoneNumber;
    boolean phoneNumberVerified;
    LocalDateTime createdAt;
    AccountSettingResponse accountSetting;
}