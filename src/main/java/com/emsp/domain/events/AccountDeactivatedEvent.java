package com.emsp.domain.events;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.Account;
import java.time.Instant;

/**
 * 账户停用事件
 * 
 * <p>当账户被停用时触发此事件</p>
 */
public class AccountDeactivatedEvent implements DomainEvent {
    private final Long accountId;
    private final String email;
    private final Instant deactivatedAt;
    private final String reason; // 可选：停用原因

    /**
     * 构造函数
     * 
     * @param account 被停用的账户
     */
    public AccountDeactivatedEvent(Account account) {
        this.accountId = account.getId();
        this.email = account.getEmail();
        this.deactivatedAt = Instant.now();
        this.reason = "Manual deactivation"; // 默认原因
    }

    /**
     * 构造函数（带停用原因）
     * 
     * @param account 被停用的账户
     * @param reason 停用原因
     */
    public AccountDeactivatedEvent(Account account, String reason) {
        this.accountId = account.getId();
        this.email = account.getEmail();
        this.deactivatedAt = Instant.now();
        this.reason = reason;
    }

    // ================== Getters ================== //
    
    public Long getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public Instant getDeactivatedAt() {
        return deactivatedAt;
    }

    public String getReason() {
        return reason;
    }

    // ================== 事件元数据 ================== //

    @Override
    public String eventName() {
        return "AccountDeactivatedEvent";
    }

    @Override
    public String aggregateType() {
        return "Account";
    }

    @Override
    public String aggregateId() {
        return accountId.toString();
    }

    @Override
    public String toString() {
        return "AccountDeactivatedEvent{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", deactivatedAt=" + deactivatedAt +
                ", reason='" + reason + '\'' +
                '}';
    }
}