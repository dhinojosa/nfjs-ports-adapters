package com.evolutionnext.domain.events.order;


public record OrderCanceled(java.time.LocalDateTime order) implements OrderEvent {
}
