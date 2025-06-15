package com.emsp.domain.service;

import com.emsp.domain.model.Account;
import com.emsp.domain.model.AccountStatus;
import com.emsp.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(String email) {
        // 验证邮箱是否为空
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // 检查邮箱是否已存在
        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException("Account with this email already exists");
        }

        // 创建新账户
        Account account = Account.create(email);
        return accountRepository.save(account);
    }

    @Override
    public void changeAccountStatus(Long accountId, AccountStatus newStatus) {
        // 验证账户ID
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        // 查找账户
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        // 根据新状态执行相应操作
        switch (newStatus) {
            case ACTIVATED:
                account.activate();
                break;
            case DEACTIVATED:
                account.deactivate();
                break;
            default:
                throw new IllegalArgumentException("Invalid status transition: " + newStatus);
        }

        // 保存更新后的账户
        accountRepository.save(account);
    }

    @Override
    public List<Account> findByLastUpdatedAfter(Instant lastUpdated, int page, int size) {
        // 验证参数
        if (lastUpdated == null) {
            throw new IllegalArgumentException("Last updated timestamp cannot be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be > 0");
        }

        // 调用仓储实现分页查询
        return accountRepository.findByLastUpdatedAfter(lastUpdated, page, size);
    }
}