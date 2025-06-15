package com.emsp.domain.repository;

import com.emsp.domain.model.Account;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);
    List<Account> findByLastUpdatedAfter(Instant lastUpdated, int page, int size);
}