package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;

import java.util.List;

public class CardsQueryRequestEvent extends DomainEvent {
    private final String requestId;
    private final List<Long> accountIds;
    
    public CardsQueryRequestEvent(String requestId, List<Long> accountIds) {
        super(requestId);
        this.requestId = requestId;
        this.accountIds = accountIds;
    }
    
    // 实现 DomainEvent 接口方法
    @Override
    public String eventName() {
        return "CardsQueryRequestEvent";
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public List<Long> getAccountIds() {
        return accountIds;
    }
}