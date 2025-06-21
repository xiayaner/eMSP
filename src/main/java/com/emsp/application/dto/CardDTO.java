package com.emsp.application.dto;

import com.emsp.domain.model.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.Instant;

@Data
public class CardDTO {
    @Schema(description = "Unique identifier of the card", example = "456")
    private Long id;
    @Schema(description = "Unique identifier (UID) of the card", example = "CARD-12345")
    private String uid;
    @Schema(description = "Visible number of the card", example = "1234-5678-9012-3456")
    private String visibleNumber;
    @Schema(description = "Contract ID in EMAID format", example = "QR567STU567890")
    private String contractId;
    @Schema(description = "Status of the card",
            example = "ACTIVATED",
            allowableValues = {"CREATED", "ASSIGNED", "ACTIVATED", "DEACTIVATED"})
    private CardStatus status;
    @Schema(description = "ID of the account the card is assigned to", example = "123")
    private Long accountId;
    @Schema(description = "Creation date of the card", example = "2023-01-01T00:00:00Z")
    private Instant createdDate;
    @Schema(description = "Last update date of the card", example = "2023-01-01T00:00:00Z")
    private Instant lastUpdated;
}