package com.zaicev.task_tracker_user_sevice.mapper;


import com.zaicev.task_tracker_user_sevice.dto.AccountSettingResponse;
import com.zaicev.task_tracker_user_sevice.model.AccountSetting;
import com.zaicev.task_tracker_user_sevice.model.NotificationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSettingMapperTest {

    private final AccountSettingMapper accountSettingMapper = new AccountSettingMapper();

    private final AccountSetting accountSetting = new AccountSetting(1L, true, NotificationType.EMAIL);

    @Test
    void toAccountSettingResponse_ShouldMapAccountSettingToAccountSettingResponse() {

        AccountSettingResponse result = accountSettingMapper.toAccountSettingResponse(accountSetting);

        assertNotNull(result);
        assertTrue(result.isNeedNotification());
        assertEquals(NotificationType.EMAIL, result.getNotificationType());
    }
}
