package com.evolutionnext.application.commands.product;


import java.math.BigDecimal;

public record CreateProduct(String name, BigDecimal price) implements ProductCommand {
}
