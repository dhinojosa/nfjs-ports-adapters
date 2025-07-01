package com.evolutionnext.application.commands.product;


public record CreateProduct(String name, float price) implements ProductCommand {
}
