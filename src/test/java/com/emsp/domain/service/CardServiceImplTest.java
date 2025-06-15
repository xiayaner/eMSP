package com.emsp.domain.service;

import com.emsp.domain.model.Account;
import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCard_shouldSuccess() {
        RFID rfid = new RFID("uid123", "1234-5678");
        ContractId contractId = new ContractId("QR567STU567890");

        when(cardRepository.findByRfid(any(RFID.class))).thenReturn(Optional.empty());
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Card card = cardService.createCard(rfid, contractId);

        assertNotNull(card);
        assertEquals(rfid, card.getRfid());
        assertEquals(contractId, card.getContractId());
        assertEquals(CardStatus.CREATED, card.getStatus());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_shouldThrowWhenUidExists() {
        RFID rfid = new RFID("uid123", "1234-5678");
        ContractId contractId = new ContractId("QR567STU567890");

        when(cardRepository.findByRfid(any(RFID.class))).thenReturn(Optional.of(
                Card.create(new RFID("uid124", "1234-5679"), new ContractId("QR567STU567891"))));

        assertThrows(IllegalArgumentException.class, 
            () -> cardService.createCard(rfid, contractId));
    }

    @Test
    void assignCardToAccount_shouldSuccess() {
        String cardId = "card-1";
        Long accountId = 1L;
        Card card = Card.create(new RFID("uid123", "1234-5678"), new ContractId("QR567STU567890"));
        Account account = Account.create("test@example.com");

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        cardService.assignCardToAccount(cardId, accountId);

        assertEquals(accountId, card.getAccountId());
        assertEquals(CardStatus.ASSIGNED, card.getStatus());
    }

    @Test
    void assignCardToAccount_shouldThrowWhenCardAlreadyAssigned() {
        String cardId = "card-1";
        Long accountId = 1L;
        Card card = Card.create(new RFID("uid123", "1234-5678"), new ContractId("QR567STU567890"));
        card.assignToAccount(accountId);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(IllegalStateException.class, 
            () -> cardService.assignCardToAccount(cardId, accountId));
    }
}