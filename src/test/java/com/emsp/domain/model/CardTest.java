package com.emsp.domain.model;

import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void createCard_shouldHaveInitialState() {
        RFID rfid = new RFID("uid123", "1234-5678");
        ContractId contractId = new ContractId("ST890VWX890123");
        Card card = Card.create(rfid, contractId);

        assertNull(card.getId());
        assertEquals(rfid, card.getRfid());
        assertEquals(contractId, card.getContractId());
        assertEquals(CardStatus.CREATED, card.getStatus());
        assertNull(card.getAccountId());
        assertNotNull(card.getCreatedDate());
        assertNotNull(card.getLastUpdated());
    }

    @Test
    void assignToAccount_shouldChangeStatus() {
        Card card = Card.create(new RFID("uid123", "1234-5678"), new ContractId("ST890VWX890123"));
        Account account = Account.create("test@example.com");
        Long accountId = 124L;
        card.assignToAccount(accountId);

        assertEquals(accountId, card.getAccountId());
        assertEquals(CardStatus.ASSIGNED, card.getStatus());
        assertTrue(card.getLastUpdated().equals(card.getCreatedDate()) ||
                card.getLastUpdated().isAfter(card.getCreatedDate()));
    }

    @Test
    void activate_shouldChangeStatus() {
        Card card = Card.create(new RFID("uid123", "1234-5678"), new ContractId("ST890VWX890123"));
        Account account = Account.create("test@example.com");
        account.setId(124L);
        card.assignToAccount(account.getId());
        
        card.activate();
        
        assertEquals(CardStatus.ACTIVATED, card.getStatus());
    }
}