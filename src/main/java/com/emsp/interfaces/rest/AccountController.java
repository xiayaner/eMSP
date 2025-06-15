package com.emsp.interfaces.rest;

import com.emsp.application.AccountAppService;
import com.emsp.application.dto.AccountDTO;
import com.emsp.application.dto.AccountWithCardsDTO;
import com.emsp.application.dto.CreateAccountRequest;
import com.emsp.application.dto.UpdateAccountStatusRequest;
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
public class AccountController {

    private final AccountAppService accountAppService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountDTO account = accountAppService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateAccountStatus(
            @PathVariable Long id,
            @RequestBody UpdateAccountStatusRequest request) {
        accountAppService.updateAccountStatus(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AccountWithCardsDTO>> searchAccountsWithCards(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastUpdated,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<AccountWithCardsDTO> result = accountAppService.findAccountsWithCardsUpdatedAfter(
                lastUpdated, page, size);

        return ResponseEntity.ok(result);
    }
}