/*
 FIX:
     1. Change all the enum to snake case
     2. Converted CHAR to VARCHAR when necessary
     3. Added name to foreign key constraint when necessary
 AUTHOR: test-in-prod-10x
 SINCE: 03-03-2026
*/

ALTER TABLE `user`
    MODIFY COLUMN role ENUM(
        'admin',
        'organizer',
        'venue_manager',
        'finance_officer',
        'attendee',
        'vendor'
    ) NOT NULL;

ALTER TABLE `report`
    MODIFY COLUMN metrics JSON;

ALTER TABLE `auditlog`
    ADD COLUMN entityId CHAR(36) NOT NULL,
    ADD COLUMN entityName VARCHAR(255),
    ADD COLUMN createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    MODIFY COLUMN action VARCHAR(100) NOT NULL,
    MODIFY COLUMN resource VARCHAR(100) NOT NULL;

ALTER TABLE `schedule`
    MODIFY timeSlot VARCHAR(100);

ALTER TABLE `ticket`
    MODIFY COLUMN status ENUM('active', 'inactive') DEFAULT 'active';

ALTER TABLE `registration`
    MODIFY COLUMN status ENUM('pending','confirmed','cancelled','checked_in') DEFAULT 'pending';

ALTER TABLE `resource`
    MODIFY COLUMN availability ENUM('available','in_use','unavailable') DEFAULT 'available';

DROP TABLE `delivery`;

CREATE TABLE IF NOT EXISTS `delivery`(
    deliveryId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    invoiceId CHAR(36) NOT NULL REFERENCES `invoice`(invoiceId)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
    item CHAR(100) NOT NULL,
    quantity INT NOT NULL,
    deliveryDate TIMESTAMP,
    status ENUM(
        'scheduled',
        'in_transit',
        'delivered',
        'failed',
        'cancelled'
    ) DEFAULT 'scheduled',
    trackingNumber VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_delivery_invoice FOREIGN KEY (invoiceId) REFERENCES invoice(invoiceId)
);

ALTER TABLE `payment`
    MODIFY COLUMN method ENUM(
        'credit_card',
        'debit_card',
        'bank_transfer',
        'cash',
        'upi',
        'paypal'
    ) NOT NULL;

