package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Card;
import java.util.List;
import java.util.Map;

public class CardsQueryResponseEvent extends DomainEvent {
    private final String requestId;
    private final Map<Long, List<Card>> cardsByAccountId;
    
    public CardsQueryResponseEvent(String requestId, Map<Long, List<Card>> cardsByAccountId) {
        super(requestId);
        this.requestId = requestId;
        this.cardsByAccountId = cardsByAccountId;
    }
    
    // 实现 DomainEvent 接口方法
    @Override
    public String eventName() {
        return "CardsQueryResponseEvent";
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public Map<Long, List<Card>> getCardsByAccountId() {
        return cardsByAccountId;
    }
}