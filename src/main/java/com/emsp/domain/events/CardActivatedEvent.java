package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Card;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CardActivatedEvent implements DomainEvent {
    
    private final Card card;
    private final Instant occurredAt;

    public CardActivatedEvent(Card card) {
        this.card = card;
        this.occurredAt = Instant.now();
    }

    @Override
    public String eventName() {
        return "CardActivatedEvent";
    }

    @Override
    public String aggregateType() {
        return "Card";
    }

    @Override
    public String aggregateId() {
        return card.getId().toString();
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    public Object payload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("cardId", card.getId());
        payload.put("activatedAt", occurredAt.toString());
        payload.put("contractId", card.getContractId().getValue());
        return Collections.unmodifiableMap(payload);
    }
}