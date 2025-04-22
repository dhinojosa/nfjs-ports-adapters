package com.evolutionnext.domain.aggregates.order;


public record OrderCanceled(java.time.LocalDateTime order) implements OrderEvent {
}
