package com.emsp.interfaces.rest;

import com.emsp.application.AccountAppService;
import com.emsp.application.dto.AccountDTO;
import com.emsp.application.dto.AccountWithCardsDTO;
import com.emsp.application.dto.CreateAccountRequest;
import com.emsp.application.dto.UpdateAccountStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing accounts")
public class AccountController {

    private final AccountAppService accountAppService;

    @PostMapping
    @Operation(summary = "Create a new account",
            description = "Creates a new account with the provided email")
    @ApiResponse(responseCode = "201", description = "Account created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountDTO.class)))
    public ResponseEntity<AccountDTO> createAccount(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account creation request",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateAccountRequest.class))) @Valid @RequestBody CreateAccountRequest request) {
        AccountDTO account = accountAppService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status",
            description = "Updates the status of an existing account")
    @ApiResponse(responseCode = "204", description = "Account status updated successfully")
    public ResponseEntity<Void> updateAccountStatus(
            @Parameter(description = "ID of the account to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account status update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateAccountStatusRequest.class)))
            @RequestBody UpdateAccountStatusRequest request) {
        accountAppService.updateAccountStatus(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search accounts with cards updated after a specific time",
            description = "Returns a paginated list of accounts with their cards updated after the given timestamp")
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountWithCardsDTO.class)))
    public ResponseEntity<Page<AccountWithCardsDTO>> searchAccountsWithCards(
            @Parameter(description = "Last updated timestamp (ISO format)", example = "2023-01-01T00:00:00Z", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastUpdated,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        Page<AccountWithCardsDTO> result = accountAppService.findAccountsWithCardsUpdatedAfter(
                lastUpdated, page, size);

        return ResponseEntity.ok(result);
    }
}