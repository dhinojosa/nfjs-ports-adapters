package com.evolutionnext.application.port.in.product;


import com.evolutionnext.application.commands.product.ProductCommand;
import com.evolutionnext.application.results.product.command.ProductResult;

public interface ForAdminProductCommandPort {
    ProductResult execute(ProductCommand command) throws Exception;
}
