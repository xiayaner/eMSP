package com.emsp.application.dto;


import com.emsp.domain.model.AccountStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AccountDTO {
    private Long id;
    private String email;
    private AccountStatus status;
    private Instant createdDate;
    private Instant lastUpdated;
    private List<CardDTO> cards;
}