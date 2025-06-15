package com.emsp.application.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateCardRequest {
    @NotBlank(message = "UID cannot be blank")
    private String uid;
    
    @NotBlank(message = "Visible number cannot be blank")
    private String visibleNumber;
    
    @NotBlank(message = "Contract ID cannot be blank")
    @Pattern(regexp = "(?i)^[A-Z]{2}[\\dA-Z]{3}[\\dA-Z]{9}$",
             message = "Invalid EMAID format for contract ID")
    private String contractId;
}