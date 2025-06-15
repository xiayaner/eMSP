package com.emsp.infrastructure.event;

import com.emsp.domain.common.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}