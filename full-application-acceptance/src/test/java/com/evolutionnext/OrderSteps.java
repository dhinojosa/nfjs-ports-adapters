package com.evolutionnext;

import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.product.ProductId;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ScenarioScoped
public class OrderSteps {

    private final Order order;

    @Inject
    public OrderSteps(Order order) {
        this.order = order;
    }

    @Given("a new order")
    public void aNewOrder(){
    }

    @Then("the total should be {int}")
    public void theTotalShouldBe(int total) {
        assertThat(order.total()).isEqualTo(total);
    }

    @When("I add the following order items:")
    public void iAddTheFollowingOrderItems(io.cucumber.datatable.DataTable dataTable) {
        var entries = dataTable.asMaps();
        System.out.println(entries);
        for (var entry : entries) {
            UUID productId = UUID.fromString(entry.get("product_id"));
            int quantity = Integer.parseInt(entry.get("quantity"));
            int amount = Integer.parseInt(entry.get("amount"));
            order.addOrderItem(new ProductId(productId), quantity, BigDecimal.valueOf(amount));
        }
    }
}
