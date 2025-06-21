package com.emsp.infrastructure.event;

import com.emsp.domain.events.AccountDeactivatedEvent;
import com.emsp.domain.service.CardService;
import org.springframework.stereotype.Component;

@Component
public class AccountDeactivationEventHandler {

    private final CardService cardService;

    public AccountDeactivationEventHandler(CardService cardService) {
        this.cardService = cardService;
    }

    public void handle(AccountDeactivatedEvent event) {
        System.out.println("Handling account deactivation event for account: " + event.getAccountId());
        
        // 停用该账户下的所有卡片
        cardService.deactivateCardsForAccount(event.getAccountId());
    }
}