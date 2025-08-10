package com.zaicev.task_tracker_account_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "email_verified")
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_number_verified")
    @Builder.Default
    private boolean phoneNumberVerified = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_setting_id", referencedColumnName = "id")
    @Builder.Default
    private AccountSetting accountSetting = new AccountSetting();

}
