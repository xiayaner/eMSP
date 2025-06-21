package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Account;

import java.time.Instant;

public class AccountDeactivatedEvent extends DomainEvent {
    private final Long accountId;
    private final String email;
    private final Instant deactivatedAt;
    private final String reason;

    public AccountDeactivatedEvent(Account account) {
        super(account); // 传递事件源
        this.accountId = account.getId();
        this.email = account.getEmail();
        this.deactivatedAt = Instant.now();
        this.reason = "Manual deactivation";
    }
    
    // 实现基类方法
    @Override
    public String eventName() {
        return "AccountDeactivatedEvent";
    }
    
    // Getters
    public Long getAccountId() {
        return accountId;
    }
}