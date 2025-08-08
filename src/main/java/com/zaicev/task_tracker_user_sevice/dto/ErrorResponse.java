package com.zaicev.task_tracker_user_sevice.dto;

import lombok.Value;

import java.time.Instant;

@Value
public class ErrorResponse {
    String message;
    int statusCode;
    Instant timestamp;
}
