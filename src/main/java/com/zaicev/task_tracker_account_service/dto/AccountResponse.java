package com.zaicev.task_tracker_account_service.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.zaicev.task_tracker_account_service.model.Account}
 */
@Value
@Builder
public class AccountResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    boolean emailVerified;
    String phoneNumber;
    boolean phoneNumberVerified;
    AccountSettingResponse accountSetting;
}