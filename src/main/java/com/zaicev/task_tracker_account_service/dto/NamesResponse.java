package com.zaicev.task_tracker_account_service.dto;

import lombok.Value;

/**
 * DTO for {@link com.zaicev.task_tracker_account_service.model.Account}
 */
@Value
public class NamesResponse {
    String firstName;
    String lastName;
}