package com.evolutionnext.application.commands.product;


import java.math.BigDecimal;

public sealed interface ProductCommand permits ProductCommand.CreateProduct {
    public record CreateProduct(String name, BigDecimal price) implements ProductCommand {
    }
}
