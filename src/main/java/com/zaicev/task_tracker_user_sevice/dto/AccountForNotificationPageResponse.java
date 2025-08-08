package com.zaicev.task_tracker_user_sevice.dto;

import lombok.Value;

import java.util.List;

@Value
public class AccountForNotificationPageResponse {
    List<AccountForNotificationResponse> accountForNotificationResponses;
    boolean hasNext;
}
