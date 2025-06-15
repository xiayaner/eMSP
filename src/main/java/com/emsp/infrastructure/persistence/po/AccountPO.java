package com.emsp.infrastructure.persistence.po;

import com.emsp.domain.model.AccountStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class AccountPO {
    private Long id;
    private String email;
    private AccountStatus status;
    private Instant createdDate;
    private Instant lastUpdated;
}