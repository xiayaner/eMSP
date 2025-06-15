package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Card;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CardAssignedEvent implements DomainEvent {

    private final Card card;
    private final Instant occurredAt;

    public CardAssignedEvent(Card card) {
        this.card = card;
        this.occurredAt = Instant.now();
    }

    @Override
    public String eventName() {
        return "CardAssignedEvent";
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
        payload.put("accountId", card.getAccountId());
        payload.put("assignedAt", occurredAt.toString());
        return payload;
    }
}