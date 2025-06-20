package com.emsp.infrastructure.persistence.repository;

import com.emsp.infrastructure.event.DomainEventPublisher;
import com.emsp.infrastructure.persistence.converter.AccountConverter;
import com.emsp.domain.model.Account;
import com.emsp.domain.repository.AccountRepository;
import com.emsp.infrastructure.persistence.mapper.AccountMapper;
import com.emsp.infrastructure.persistence.po.AccountPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountMapper accountMapper;
    private final AccountConverter accountConverter;
    private final DomainEventPublisher domainEventPublisher; // 注入事件发布器


    @Override
    public Account save(Account account) {
        AccountPO po = accountConverter.toPO(account);
        if (account.getId() == null) {
            accountMapper.insert(po);
            account.setId(po.getId());
        } else {
            accountMapper.update(po);
        }
        // 关键：保存后发布所有领域事件
        publishDomainEvents(account);
        return account;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountMapper.selectById(id))
                .map(accountConverter::toDomain);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return Optional.ofNullable(accountMapper.selectByEmail(email))
                .map(accountConverter::toDomain);
    }

    @Override
    public List<Account> findByLastUpdatedAfter(Instant lastUpdated, int page, int size) {
        return accountMapper.selectByLastUpdatedAfter(lastUpdated, page, size).stream()
                .map(accountConverter::toDomain)
                .collect(Collectors.toList());
    }

    private void publishDomainEvents(Account account) {
        account.getDomainEvents().forEach(domainEventPublisher::publish);
        account.clearDomainEvents();
    }
}