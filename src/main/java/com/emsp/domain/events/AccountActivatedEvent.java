package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Account;

public class AccountActivatedEvent implements DomainEvent {
    private final Long accountId;
    private final String email;
    
    public AccountActivatedEvent(Account account) {
        this.accountId = account.getId();
        this.email = account.getEmail();
    }
    
    // Getters
    public Long getAccountId() {
        return accountId;
    }
    
    public String getEmail() {
        return email;
    }

    @Override
    public String eventName() {
        return "";
    }

    @Override
    public String aggregateType() {
        return "";
    }

    @Override
    public String aggregateId() {
        return "";
    }
}