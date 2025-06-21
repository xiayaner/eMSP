package com.emsp.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateCardRequest {
    @Schema(description = "Unique identifier (UID) for the card",
            example = "CARD-12345",
            required = true)
    @NotBlank(message = "UID cannot be blank")
    private String uid;

    @Schema(description = "Visible number for the card",
            example = "1234-5678-9012-3456",
            required = true)
    @NotBlank(message = "Visible number cannot be blank")
    private String visibleNumber;

    @Schema(description = "Contract ID in EMAID format",
            example = "QR567STU567890",
            required = true)
    @NotBlank(message = "Contract ID cannot be blank")
    @Pattern(regexp = "(?i)^[A-Z]{2}[\\dA-Z]{3}[\\dA-Z]{9}$",
             message = "Invalid EMAID format for contract ID")
    private String contractId;
}