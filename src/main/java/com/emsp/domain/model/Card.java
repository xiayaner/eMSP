package com.emsp.domain.model;

import com.emsp.domain.common.DomainEvent;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class Card {
    private Long id;
    private final RFID rfid;
    private final ContractId contractId;
    private CardStatus status;
    private Long accountId;
    private final Instant createdDate;
    private Instant lastUpdated;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // ================== 工厂方法 ================== //

    /**
     * 创建新卡
     *
     * @param rfid RFID值对象
     * @param contractId 合约ID值对象
     * @return 新创建的卡对象（状态为CREATED）
     */
    public static Card create(RFID rfid, ContractId contractId) {
        return new Card(null, rfid, contractId, CardStatus.CREATED,
                null, Instant.now(), Instant.now());
    }

    /**
     * 重建卡对象（从持久化存储）
     *
     * @param id 卡ID
     * @param rfid RFID值对象
     * @param contractId 合约ID值对象
     * @param status 卡状态
     * @param accountId 关联账户ID
     * @param createdDate 创建时间
     * @param lastUpdated 最后更新时间
     * @return 重建的卡对象
     */
    public static Card reconstruct(Long id, RFID rfid, ContractId contractId,
                                   CardStatus status, Long accountId,
                                   Instant createdDate, Instant lastUpdated) {
        return new Card(id, rfid, contractId, status, accountId,
                createdDate, lastUpdated);
    }

    public Card(Long id, RFID rfid, ContractId contractId,
                 CardStatus status, Long accountId,
                 Instant createdDate, Instant lastUpdated) {
        this.id = id;
        this.rfid = Objects.requireNonNull(rfid, "RFID cannot be null");
        this.contractId = Objects.requireNonNull(contractId, "Contract ID cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.accountId = accountId;
        this.createdDate = Objects.requireNonNull(createdDate, "Created date cannot be null");
        this.lastUpdated = Objects.requireNonNull(lastUpdated, "Last updated cannot be null");
    }

    // ================== 领域行为 ================== //

    /**
     * 分配卡到账户
     *
     * @param accountId 账户ID
     * @throws IllegalStateException 如果卡不在CREATED状态
     */
    public void assignToAccount(Long accountId) {
        if (this.status != CardStatus.CREATED) {
            throw new IllegalStateException("Card must be in CREATED status for assignment");
        }
        if (this.accountId != null) {
            throw new IllegalStateException("Card is already assigned to an account");
        }

        this.accountId = accountId;
        this.status = CardStatus.ASSIGNED;
        this.lastUpdated = Instant.now();
    }

    /**
     * 激活卡
     *
     * @throws IllegalStateException 如果卡不在ASSIGNED状态
     */
    public void activate() {
        if (this.status != CardStatus.ASSIGNED) {
            throw new IllegalStateException("Card must be in ASSIGNED status to activate");
        }
        this.status = CardStatus.ACTIVATED;
        this.lastUpdated = Instant.now();
    }

    /**
     * 停用卡
     *
     * @throws IllegalStateException 如果卡不在ACTIVATED状态
     */
    public void deactivate() {
        if (this.status != CardStatus.ACTIVATED) {
            throw new IllegalStateException("Card must be in ACTIVATED status to deactivate");
        }
        this.status = CardStatus.DEACTIVATED;
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

    // ================== 对象标识 ================== //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) &&
                Objects.equals(rfid, card.rfid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rfid);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", rfid=" + rfid +
                ", contractId=" + contractId +
                ", status=" + status +
                ", accountId=" + accountId +
                '}';
    }
}