package com.zaicev.task_tracker_user_sevice.repository;

import com.zaicev.task_tracker_user_sevice.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.accountSetting.needNotification = true")
    Page<Account> findAllAccountWithNeedNotification(Pageable pageable);
}
