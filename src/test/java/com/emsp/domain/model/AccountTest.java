package com.emsp.domain.model;

import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void createAccount_shouldHaveInitialState() {
        String email = "test@example.com";
        Account account = Account.create(email);

        assertNull(account.getId());
        assertEquals(email, account.getEmail());
        assertEquals(AccountStatus.CREATED, account.getStatus());
        assertNotNull(account.getCreatedDate());
        assertNotNull(account.getLastUpdated());
    }

    @Test
    void activate_shouldChangeStatus() {
        Account account = Account.create("test@example.com");
        account.activate();

        assertEquals(AccountStatus.ACTIVATED, account.getStatus());
        assertTrue(account.getLastUpdated().equals(account.getCreatedDate()) ||
                account.getLastUpdated().isAfter(account.getCreatedDate()));
    }

    @Test
    void activate_shouldThrowIfNotCreated() {
        Account account = Account.create("test@example.com");
        account.activate();

        assertThrows(IllegalStateException.class, account::activate);
    }

    @Test
    void assignCard_shouldAddCard() {
        Account account = Account.create("test@example.com");
        Card card = Card.create(new RFID("uid123", "1234-5678"), new ContractId("ST890VWX890123"));
        
        account.assignCard(card);
        
        assertTrue(account.getCards().contains(card));
    }
}