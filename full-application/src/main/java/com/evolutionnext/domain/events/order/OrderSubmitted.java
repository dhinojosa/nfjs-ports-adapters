package com.evolutionnext.domain.events.order;


public record OrderSubmitted(java.time.LocalDateTime order) implements OrderEvent {
}
