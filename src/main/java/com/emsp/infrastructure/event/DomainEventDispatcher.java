package com.emsp.infrastructure.event;

import com.emsp.application.query.QueryCoordinator;
import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.events.AccountDeactivatedEvent;
import com.emsp.domain.events.CardsQueryRequestEvent;
import com.emsp.domain.events.CardsQueryResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventDispatcher {

    private final AccountDeactivationEventHandler accountDeactivationEventHandler;
    private final CardQueryEventHandler cardQueryEventHandler;
    private final QueryCoordinator queryCoordinator;

    @EventListener
    public void handleDomainEvent(DomainEvent event) {
        System.out.println("Received domain event: " + event.eventName());

        if (event instanceof AccountDeactivatedEvent) {
            accountDeactivationEventHandler.handle((AccountDeactivatedEvent) event);
        } else if (event instanceof CardsQueryRequestEvent) {
            cardQueryEventHandler.handle((CardsQueryRequestEvent) event);
        } else if (event instanceof CardsQueryResponseEvent) {
            queryCoordinator.handleResponse((CardsQueryResponseEvent) event);
        }
    }
}