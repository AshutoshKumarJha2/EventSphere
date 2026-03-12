/*
    * This modification fixes contract id to be char(36)
    * Author: test-in-prod-10x
*/



DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `delivery`;
DROP TABLE IF EXISTS `invoice`;

CREATE TABLE IF NOT EXISTS `invoice`(
    invoiceId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    contractId CHAR(36) NOT NULL,
    issueDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dueDate TIMESTAMP,
    totalAmount DECIMAL(10,2) NOT NULL,
    status ENUM(
        'issued',
        'paid',
        'overdue',
        'cancelled'
    ) DEFAULT 'issued',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_invoice_contract FOREIGN KEY (contractId) REFERENCES `contract`(contractId)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `delivery`(
    deliveryId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    invoiceId CHAR(36) NOT NULL,
    item CHAR(100) NOT NULL,
    quantity INT NOT NULL,
    deliveryDate TIMESTAMP,
    status ENUM(
        'scheduled',
        'in-transit',
        'delivered',
        'failed',
        'cancelled'
    ) DEFAULT 'scheduled',
    trackingNumber VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_delivery_invoice FOREIGN KEY  (invoiceId) REFERENCES invoice(invoiceId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ASUTOSH: PAYMENT TABLE
CREATE TABLE IF NOT EXISTS `payment`(
    paymentId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    invoiceId CHAR(36) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    method ENUM(
        'credit-card',
        'debit-card',
        'bank-transfer',
        'cash',
        'upi',
        'paypal'
    ) NOT NULL,
    status ENUM(
        'pending',
        'completed',
        'failed',
        'refunded'
    ) DEFAULT 'pending',
    paymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_invoice FOREIGN KEY (invoiceId) REFERENCES `invoice`(invoiceId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);