package com.emsp.domain.model;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.events.AccountDeactivatedEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Account {
    private Long id;
    private final String email;
    private AccountStatus status;
    private final Instant createdDate;
    private Instant lastUpdated;
    private final Set<Card> cards = new HashSet<>();

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // 工厂方法 - 创建新账户
    public static Account create(String email) {
        return new Account(null, email, AccountStatus.CREATED, Instant.now(), Instant.now());
    }

    // 重建方法 - 从持久化重建
    public static Account reconstruct(Long id, String email, AccountStatus status,
                                      Instant createdDate, Instant lastUpdated) {
        return new Account(id, email, status, createdDate, lastUpdated);
    }

    public Account(String email) {
        this.email = email;
        this.status = AccountStatus.CREATED;
        this.createdDate = Instant.now();
        this.lastUpdated = Instant.now();
    }

    private Account(Long id, String email, AccountStatus status,
                    Instant createdDate, Instant lastUpdated) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    // ================== 领域行为 ================== //

    /**
     * 激活账户
     *
     * @throws IllegalStateException 如果账户不在CREATED状态
     */
    public void activate() {
        if (this.status != AccountStatus.CREATED) {
            throw new IllegalStateException("Account must be in CREATED status to activate");
        }
        this.status = AccountStatus.ACTIVATED;
        this.lastUpdated = Instant.now();
    }

    /**
     * 停用账户
     *
     * @throws IllegalStateException 如果账户不在ACTIVATED状态
     */
    public void deactivate() {
        if (this.status != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Account must be in ACTIVATED status to deactivate");
        }
        this.status = AccountStatus.DEACTIVATED;
        this.lastUpdated = Instant.now();
        domainEvents.add(new AccountDeactivatedEvent(this));
    }

    /**
     * 分配卡到账户
     *
     * @param card 要分配的卡
     * @throws IllegalStateException 如果卡不在CREATED状态
     * @throws IllegalArgumentException 如果卡为null或已分配给当前账户
     */
    public void assignCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (cards.contains(card)) {
            throw new IllegalArgumentException("Card is already assigned to this account");
        }
        if (card.getStatus() != CardStatus.CREATED) {
            throw new IllegalStateException("Card must be in CREATED status for assignment");
        }

        cards.add(card);
        card.assignToAccount(this.id);
        this.lastUpdated = Instant.now();
    }

    // ================== 领域事件管理 ================== //

    /**
     * 获取领域事件列表（不可修改）
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 清除所有领域事件
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // ================== 内部方法 ================== //

    /**
     * 设置ID（仅限基础设施层使用）
     */
    public void setId(Long id) {
        this.id = id;
    }
}