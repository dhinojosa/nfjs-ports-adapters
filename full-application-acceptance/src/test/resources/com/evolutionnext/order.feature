Feature: Order Tax Calculations
    As a customer
    When I create an order with a number of items and quantity
    The total should reflect the items, quantity added

    Scenario: Calculate total on a regular order amount
        Given a new order
        When I add the following order items:
            | product_id                           | quantity | amount |
            | 123e4567-e89b-12d3-a456-426614174000 | 2        | 100    |
            | 123e4567-e89b-12d3-a456-426614174002 | 9        | 200    |
        Then the total should be 2000

