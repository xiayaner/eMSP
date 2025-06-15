package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Card;
import java.time.Instant;

public class CardDeactivatedEvent implements DomainEvent {
    
    private final Card card;
    private final String reason;
    private final Instant occurredAt;

    public CardDeactivatedEvent(Card card, String reason) {
        this.card = card;
        this.reason = reason;
        this.occurredAt = Instant.now();
    }

    public CardDeactivatedEvent(Card card) {
        this.card = card;
        this.reason = "";
        this.occurredAt = Instant.now();
    }

    @Override
    public String eventName() {
        return "CardDeactivatedEvent";
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
        return new java.util.HashMap<String, Object>() {{
            put("cardId", card.getId());
            put("deactivatedAt", occurredAt.toString());
            put("reason", reason);
            put("contractId", card.getContractId().getValue());
        }};
    }
}