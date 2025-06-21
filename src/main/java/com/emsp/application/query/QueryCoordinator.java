package com.emsp.application.query;

import com.emsp.domain.events.CardsQueryRequestEvent;
import com.emsp.domain.events.CardsQueryResponseEvent;
import com.emsp.domain.model.Card;
import com.emsp.infrastructure.event.DomainEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QueryCoordinator {
    private final DomainEventPublisher eventPublisher;
    private final Map<String, CompletableFuture<Map<Long, List<Card>>>> pendingQueries = new ConcurrentHashMap<>();
    
    public QueryCoordinator(DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public CompletableFuture<Map<Long, List<Card>>> requestCards(List<Long> accountIds) {
        String requestId = generateRequestId(accountIds);
        CompletableFuture<Map<Long, List<Card>>> future = new CompletableFuture<>();
        
        pendingQueries.put(requestId, future);
        eventPublisher.publish(new CardsQueryRequestEvent(requestId, accountIds));
        
        return future;
    }
    
    public void handleResponse(CardsQueryResponseEvent response) {
        String requestId = response.getRequestId();
        CompletableFuture<Map<Long, List<Card>>> future = pendingQueries.remove(requestId);
        
        if (future != null) {
            future.complete(response.getCardsByAccountId());
        }
    }
    
    public String generateRequestId(List<Long> accountIds) {
        return "QR-" + System.currentTimeMillis() + "-" + accountIds.hashCode();
    }
}