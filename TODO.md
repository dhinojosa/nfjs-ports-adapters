# TODO

## Goals

1. Keep it simple
2. Order commands
   1. `Order` create
   2. `Order` load
   3. `Order` addItem
   4. `Order` submit
3. Order results
   1. `Order` created
   2. `Order` loaded
   3. `OrderItem` added
   4. `Order` submitted
4. Order errors
   1. `OrderNotFound`
   2. `ProductNotFound`
   3. `InventoryInsufficient`
5. Check internal state
6. Customers
   1. `Customer` load
7. Products
   1. `Products` load




## API

1. Initialize Order
    - POST http://localhost:8080/order
    - Request: { "customerId": "uuid" }
    - Response: { "orderId": "uuid" }

2. Add Item to Cart
    - POST http://localhost:8080/order/{orderId}/items
    - Request: { "productId": "uuid", "quantity": number }
    - Response: { "orderId": "uuid", "items": [] }

3. Submit Order
    - POST http://localhost:8080/order/{orderId}/submit
    - Response: { "orderId": "uuid", "status": "SUBMITTED" }

4. Cancel Order
    - DELETE http://localhost:8080/order/{orderId}
    - Response: { "orderId": "uuid", "status": "CANCELLED" }

5. Create Product
    - POST http://localhost:8080/products
    - Request: { "name": string, "price": number }
    - Response: { "productId": "uuid" }

6. Get Product
    - GET http://localhost:8080/product/{productId}
    - Request: { "productId": "uuid" }
    - Response: { "productId": "uuid", "name": "string", "price": "number" }
   
7. Get Product List
    - GET http://localhost:8080/products
    - Response: [{ "productId": "uuid", "name": "string", "price": "number" }]

8. Create Customer
    - POST http://localhost:8080/customer
    - Request: { "name": string, "creditLimit": number }
    - Response: { "customerId": "uuid" }

9. Get Customer
    - GET http://localhost:8080/customer/{customerId}
    - Response: { "customerId": "uuid", "name": "string", "creditLimit": "number" }

10. Get Customer List
    - GET http://localhost:8080/customers
    - Response: [{ "customerId": "uuid", "name": "string", "creditLimit": "number" }]

## Test Order

1. `Out` Infrastructure: Test Containers DB Layers (Integration Only)
2. Domain (Unit Only)
3. Domain Services (Unit Only)
4. Application Services (Unit and Integration)
5. `In` Infrastructure: (Unit and Integration)
