package com.emsp.domain.repository;

import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.RFID;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);
    Optional<Card> findById(String id);
    Optional<Card> findByRfid(RFID rfid);
    List<Card> findByAccountId(Long accountId);
    List<Card> findByLastUpdatedAfter(Instant lastUpdated, int page, int size);
    List<Card> findByAccountIdIn(List<Long> accountIds);
    List<Card> findByAccountIdAndStatus(Long accountId, CardStatus status);
}