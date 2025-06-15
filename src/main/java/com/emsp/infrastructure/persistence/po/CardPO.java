package com.emsp.infrastructure.persistence.po;

import com.emsp.domain.model.CardStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class CardPO {
    private Long id;
    private String uid;
    private String visibleNumber;
    private String contractId;
    private CardStatus status;
    private Long accountId;
    private Instant createdDate;
    private Instant lastUpdated;
}