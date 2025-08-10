package com.zaicev.task_tracker_account_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "account_settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "need_notification")
    private boolean needNotification = true;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.EMAIL;
}
