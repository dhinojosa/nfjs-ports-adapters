package com.evolutionnext.domain.aggregates.order;


public record OrderSubmitted(java.time.LocalDateTime order) implements OrderEvent {
}
