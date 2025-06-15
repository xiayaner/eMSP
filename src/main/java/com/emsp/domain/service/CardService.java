package com.emsp.domain.service;

import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.model.valueobjects.ContractId;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface CardService {
    Card createCard(RFID rfid, ContractId contractId);
    void assignCardToAccount(String cardId, Long accountId);
    void changeCardStatus(String cardId, CardStatus newStatus);
    List<Card> findCardsByLastUpdatedAfter(Instant lastUpdated, int page, int size);
    Card getCardById(String cardId);
    Map<Long, List<Card>> findCardsByAccountIds(List<Long> accountIds);
}