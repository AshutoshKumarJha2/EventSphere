/*
    * This migration fixes the delivery fk constraint to add cascade
    * Modify the resource table to add name and unit
    * Add new table for resource allocation with resourceId eventId venueId and quantity
    * Add foreign key constraints to resource allocation table
*/

ALTER TABLE `delivery`
    DROP FOREIGN KEY fk_delivery_invoice;

ALTER TABLE `delivery`
    ADD CONSTRAINT fk_delivery_invoice FOREIGN KEY (invoiceId) REFERENCES invoice(invoiceId)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE `resource`
    ADD COLUMN name VARCHAR(255) NOT NULL,
    ADD COLUMN unit INT DEFAULT 0;

CREATE TABLE IF NOT EXISTS `resource_allocation` (
    allocationId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    resourceId CHAR(36) NOT NULL,
    eventId CHAR(36) NOT NULL,
    venueId CHAR(36) NOT NULL,
    quantity INT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_allocation_resource FOREIGN KEY (resourceId) REFERENCES resource(resourceId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_allocation_event FOREIGN KEY (eventId) REFERENCES event(eventId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_allocation_venue FOREIGN KEY (venueId) REFERENCES venue(venueId) ON DELETE CASCADE ON UPDATE CASCADE
);

