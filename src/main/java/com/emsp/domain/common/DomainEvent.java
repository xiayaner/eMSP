package com.emsp.domain.common;

import org.springframework.context.ApplicationEvent;

import java.time.Instant;

public abstract class DomainEvent extends ApplicationEvent {
    public DomainEvent(Object source) {
        super(source);
    }

    public abstract String eventName();
    public Instant occurredAt() {
        return Instant.now();
    }
}