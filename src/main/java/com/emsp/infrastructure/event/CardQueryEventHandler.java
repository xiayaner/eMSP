package com.emsp.infrastructure.event;

import com.emsp.domain.events.CardsQueryRequestEvent;
import com.emsp.domain.events.CardsQueryResponseEvent;
import com.emsp.domain.model.Card;
import com.emsp.domain.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CardQueryEventHandler {
    private final CardService cardService;
    private final DomainEventPublisher eventPublisher;
    
    public void handle(CardsQueryRequestEvent event) {
        Map<Long, List<Card>> cardsByAccountId =
            cardService.findCardsByAccountIds(event.getAccountIds());
        
        eventPublisher.publish(new CardsQueryResponseEvent(
            event.getRequestId(), 
            cardsByAccountId
        ));
    }
}