-- Create the Customers table
CREATE TABLE customers
(
    id           UUID PRIMARY KEY,        -- Customer ID (Primary Key)
    name         VARCHAR(255)   NOT NULL, -- Customer name
    credit_limit DECIMAL(10, 2) NOT NULL  -- Customer's credit limit
);

-- Create the Products table
CREATE TABLE products
(
    id    UUID PRIMARY KEY,        -- Product ID (Primary Key)
    name  VARCHAR(255)   NOT NULL, -- Name of the product
    price DECIMAL(10, 2) NOT NULL  -- Price of the product
);

-- Create the Orders table
CREATE TABLE orders
(
    id          UUID PRIMARY KEY,     -- Order ID (Primary Key)
    customer_id UUID        NOT NULL, -- Customer ID (Foreign Key)
    state       VARCHAR(50) NOT NULL, -- Order state (e.g., NEW, PROCESSING, COMPLETED)

    -- Foreign Key Constraint
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);

-- Create the Order_items table
CREATE TABLE order_items
(
    id         BIGINT PRIMARY KEY,      -- OrderItem ID (Primary Key)
    order_id   UUID           NOT NULL, -- Order ID (Foreign Key)
    product_id UUID           NOT NULL, -- Product ID (Foreign Key)
    quantity   INT            NOT NULL, -- Quantity of the product
    price      DECIMAL(10, 2) NOT NULL, -- Price of this particular order item
    name       VARCHAR(255)   NOT NULL, -- Name of the product

    -- Foreign Key Constraints
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES products (id)
);
