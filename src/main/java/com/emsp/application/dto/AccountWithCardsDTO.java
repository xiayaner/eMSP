package com.emsp.application.dto;

import com.emsp.domain.model.AccountStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class AccountWithCardsDTO {
    private Long accountId;
    private String email;
    private AccountStatus status;
    private Instant accountCreatedDate;
    private Instant accountLastUpdated;
    private List<CardDTO> cards;
}