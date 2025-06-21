package com.emsp.application.dto;


import com.emsp.domain.model.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AccountDTO {
    @Schema(description = "Unique identifier of the account", example = "123")
    private Long id;
    @Schema(description = "Email address of the account", example = "user@example.com")
    private String email;
    @Schema(description = "Status of the account", example = "ACTIVATED")
    private AccountStatus status;
    @Schema(description = "Creation date of the account", example = "2023-01-01T00:00:00Z")
    private Instant createdDate;
    @Schema(description = "Last update date of the account", example = "2023-01-01T00:00:00Z")
    private Instant lastUpdated;
    @Schema(description = "List of cards associated with the account")
    private List<CardDTO> cards;
}