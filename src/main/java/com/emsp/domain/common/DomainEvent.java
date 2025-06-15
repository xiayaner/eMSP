package com.emsp.domain.common;

import java.time.Instant;

/**
 * 领域事件接口
 *
 * <p>表示领域中发生的重要事情，用于解耦系统组件和实现事件驱动架构</p>
 */
public interface DomainEvent {

    /**
     * 获取事件唯一标识
     */
    default String eventId() {
        return aggregateId() + "-" + occurredAt().toEpochMilli();
    }

    /**
     * 获取事件名称
     */
    String eventName();

    /**
     * 获取事件发生的聚合根类型
     */
    String aggregateType();

    /**
     * 获取事件关联的聚合根ID
     */
    String aggregateId();

    /**
     * 获取事件发生的时间戳
     */
    default Instant occurredAt() {
        return Instant.now();
    }
}