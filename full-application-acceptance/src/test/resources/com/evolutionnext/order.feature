Feature: Order Tax Calculations
    As a customer
    When I create an order with a number of items and quantity
    The total should reflect the items, quantity added

    Scenario: Calculate total on a regular order amount
        Given a new order
        When I add the following order items:
            | product_id | quantity | amount |
            | 3          | 2        | 100    |
            | 10         | 9        | 200    |
        Then the total should be 2000

