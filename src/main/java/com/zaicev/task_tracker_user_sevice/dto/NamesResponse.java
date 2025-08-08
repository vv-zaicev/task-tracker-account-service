package com.zaicev.task_tracker_user_sevice.dto;

import lombok.Value;

/**
 * DTO for {@link com.zaicev.task_tracker_user_sevice.model.Account}
 */
@Value
public class NamesResponse {
    String firstName;
    String lastName;
}