package com.emsp.infrastructure.config;

import com.emsp.domain.common.DomainEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Configuration
@EnableTransactionManagement
public class EventConfig {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDomainEvent(DomainEvent event) {
        // Spring会自动将事件路由到DomainEventDispatcher
    }
}
