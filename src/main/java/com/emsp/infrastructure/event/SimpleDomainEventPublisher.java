package com.emsp.infrastructure.event;

import com.emsp.domain.common.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class SimpleDomainEventPublisher implements DomainEventPublisher {
    @Override
    public void publish(DomainEvent event) {
        // 这里简单打印，实际可集成Spring ApplicationEvent或消息队列
        System.out.println("Publishing domain event: " + event.getClass().getSimpleName());
    }
}