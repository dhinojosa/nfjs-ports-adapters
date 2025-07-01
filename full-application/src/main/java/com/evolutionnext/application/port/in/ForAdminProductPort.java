package com.evolutionnext.application.port.in;


import com.evolutionnext.application.commands.product.ProductCommand;
import com.evolutionnext.application.results.order.OrderResult;
import com.evolutionnext.application.results.product.ProductResult;

public interface ForAdminProductPort {
    ProductResult execute(ProductCommand command) throws Exception;
}
