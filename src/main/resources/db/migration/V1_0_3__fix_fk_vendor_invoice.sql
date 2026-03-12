DROP TABLE `invoice`;

CREATE TABLE IF NOT EXISTS `invoice`(
    invoiceId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    contractId CHAR(36) NOT NULL,
	ON UPDATE CASCADE
	ON DELETE CASCADE,
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
    CONSTRAINT fk_invoice_contract FOREIGN KEY contractId REFERENCES `contract`(contractId)
);