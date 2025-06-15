package com.emsp.domain.service;

import com.emsp.domain.model.Account;
import com.emsp.domain.model.AccountStatus;
import java.time.Instant;
import java.util.List;

public interface AccountService {

    /**
     * 创建新账户
     *
     * @param email 账户邮箱
     * @return 创建的账户对象
     * @throws IllegalArgumentException 如果邮箱已存在
     */
    Account createAccount(String email);

    /**
     * 更改账户状态
     *
     * @param accountId 账户ID
     * @param newStatus 新状态 (ACTIVATED 或 DEACTIVATED)
     * @throws IllegalArgumentException 如果账户不存在或状态转换无效
     */
    void changeAccountStatus(Long accountId, AccountStatus newStatus);

    /**
     * 根据最后更新时间查询账户
     *
     * @param lastUpdated 最后更新时间阈值
     * @param page 页码 (0-based)
     * @param size 每页大小
     * @return 账户列表
     */
    List<Account> findByLastUpdatedAfter(Instant lastUpdated, int page, int size);
}