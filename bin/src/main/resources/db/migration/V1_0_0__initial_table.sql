-- SANGNEEL: Create User Table
CREATE TABLE IF NOT EXISTS `user`(
    userId char(36) PRIMARY KEY DEFAULT (UUID()),
    name varchar(100) NOT NULL,
    role ENUM(
        'admin',
        'organizer',
        'venue manager',
        'finance officer',
        'attendee',
        'vendor'
    ) NOT NULL,
    email varchar(100) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    phone varchar(20) NOT NULL,
    status ENUM(
        'active',
        'inactive',
        'suspended'
    ) DEFAULT 'active',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ARISH VENUE TABLE
CREATE TABLE IF NOT EXISTS `venue` (
    venueId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    availabilityStatus ENUM('available','unavailable','maintenence') DEFAULT 'available',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- SANGNEEL: Create Event Table
CREATE TABLE IF NOT EXISTS `event`(
    eventId char(36) PRIMARY KEY DEFAULT (UUID()),
    name varchar(150) NOT NULL,
    organizerId char(36) NOT NULL,
    startDate datetime NOT NULL,
    endDate datetime NOT NULL,
    venueId char(36),
    status ENUM(
        'draft',
        'published',
        'completed',
        'cancelled'
    ) DEFAULT 'draft',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_organizer FOREIGN KEY (organizerId) references `user`(userId) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_event_venue FOREIGN KEY (venueId) references `venue`(venueId) ON UPDATE CASCADE ON DELETE CASCADE
);

-- TITHI: Create Report Table
CREATE TABLE IF NOT EXISTS `report` (
    reportId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    userId CHAR(36) NOT NULL,
    scope VARCHAR(100) NOT NULL,
    metrics TEXT,
    generatedDate DATETIME NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_user FOREIGN KEY (userId) REFERENCES `user`(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- HARIHARAN: create audit log
CREATE TABLE IF NOT EXISTS `auditlog` (
  auditId   CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  userId    CHAR(36) NOT NULL ,
  action    CHAR(100) NOT NULL,
  resource  CHAR(100) NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_auditlog_user FOREIGN KEY (userId) REFERENCES `user`(userId)
    	ON DELETE CASCADE 
	ON UPDATE CASCADE
);

-- HARIHARAN: create notification table
CREATE TABLE IF NOT EXISTS `notification` (
  notificationId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  userId         CHAR(36) NOT NULL,
  eventId        CHAR(36) NULL,
  message        VARCHAR(255) NOT NULL,
  category   VARCHAR(255) NOT NULL,
  status         ENUM('unread', 'read', 'archived') DEFAULT 'unread',
  createdAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_notification_user
    FOREIGN KEY (userId)  REFERENCES `user`(userId)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_notification_event
    FOREIGN KEY (eventId) REFERENCES `event`(eventId)
    ON DELETE CASCADE ON UPDATE CASCADE
);


-- HARIHARAN: create schedule table
CREATE TABLE IF NOT EXISTS `schedule` (
  scheduleId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  eventId    CHAR(36) NOT NULL,
  date       DATE NOT NULL,
  timeSlot   CHAR(36) NOT NULL,
  activity   CHAR(100) NOT NULL,
  status     ENUM('draft','active','completed','terminated') DEFAULT 'draft',
  createdAt  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_schedule_event
    FOREIGN KEY (eventId) REFERENCES `event`(eventId)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- NADIYA: create ticket table
CREATE TABLE IF NOT EXISTS `ticket` (
    ticketId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    eventId CHAR(36) NOT NULL,
    type VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_ticket_event
        FOREIGN KEY (eventId)
        REFERENCES `event`(eventId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- HARIHARAN: create registration table
CREATE TABLE IF NOT EXISTS `registration` (
  registrationId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  eventId  CHAR(36) NOT NULL,
  attendeeId     CHAR(36) NOT NULL,
  ticketId       CHAR(36) NOT NULL,
  date           DATE,
  status         ENUM('pending','confirmed','cancelled','checked-in') DEFAULT 'pending',
  createdAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_registration_event
    FOREIGN KEY (eventId)   REFERENCES `event`(eventId)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_registration_attendee
    FOREIGN KEY (attendeeId) REFERENCES `user`(userId)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_registration_ticket
    FOREIGN KEY (ticketId)  REFERENCES `ticket`(ticketId)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- NADIYA: feedback table
CREATE TABLE IF NOT EXISTS `feedback` (
    feedbackId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    eventId CHAR(36) NOT NULL,
    attendeeId CHAR(36) NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(500),
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_feedback_event
        FOREIGN KEY (eventId)
        REFERENCES `event`(eventId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_feedback_attendee
        FOREIGN KEY (attendeeId)
        REFERENCES `user`(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ARISH: create booking table
CREATE TABLE IF NOT EXISTS `booking` (
    bookingId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    eventId CHAR(36),
    venueId CHAR(36),
    date DATE NOT NULL,
    status ENUM('pending','confirmed','cancelled') DEFAULT 'pending',
 
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 
    CONSTRAINT fk_booking_event FOREIGN KEY (eventId) REFERENCES `event`(eventId)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_booking_venue FOREIGN KEY (venueId) REFERENCES `venue`(venueId)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- NADIYA: create budget table
CREATE TABLE IF NOT EXISTS `budget` (
    budgetId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    eventId CHAR(36) NOT NULL,
    plannedAmount DECIMAL(10,2) DEFAULT 0,
    actualAmount DECIMAL(10,2) DEFAULT 0,
    variance DECIMAL(10,2) DEFAULT 0,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_budget_event
        FOREIGN KEY (eventId)
        REFERENCES `event`(eventId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- NADIYA: create expense table
CREATE TABLE IF NOT EXISTS `expense` (
    expenseId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    eventId CHAR(36) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    approvedBy CHAR(36) NULL,
    status ENUM('submitted', 'approved', 'rejected','paid') DEFAULT 'submitted',
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_expense_event
        FOREIGN KEY (eventId)
        REFERENCES `event`(eventId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

        FOREIGN KEY (approvedBy)
        REFERENCES `user`(userId)
);


-- NADIYA: create engagement table
CREATE TABLE IF NOT EXISTS `engagement` (
    engagementId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    eventId CHAR(36) NOT NULL,
    attendeeId CHAR(36) NOT NULL,
    activity VARCHAR(255) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_engagement_event
        FOREIGN KEY (eventId)
        REFERENCES `event`(eventId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_engagement_attendee
        FOREIGN KEY (attendeeId)
        REFERENCES `user`(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ARIAH: create resource table
CREATE TABLE IF NOT EXISTS `resource` (
    resourceId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    venueId CHAR(36),
    type VARCHAR(100) NOT NULL,
    availability ENUM('AVAILABLE','IN_USE','UNAVAILABLE') DEFAULT 'AVAILABLE',
    costRate DECIMAL(10,2),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 
    FOREIGN KEY (venueId) REFERENCES venue(venueId)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- KAUSHIK: Vendor table
CREATE TABLE IF NOT EXISTS `vendor`(
	vendorId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
	name VARCHAR(100) NOT NULL,
	contactInfo VARCHAR(100),
	status ENUM(
		'active',
		'inactive',
		'blacklisted'
	) DEFAULT 'active',
	createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- KAUSHIK: Contract Table
CREATE TABLE IF NOT EXISTS `contract`(
	contractId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
	vendorId CHAR(36) REFERENCES `vendor`(vendorId)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	eventId CHAR(36) REFERENCES event(eventId),
	startDate DATETIME NOT NULL,
	endDate DATETIME NOT NULL,
	value DECIMAL(10,2) NOT NULL,
	status ENUM(
		'draft',
		'active',
		'completed',
		'terminated'
	) DEFAULT 'draft',
    	createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    	updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ASUTOSH: INVOICE TABLE
CREATE TABLE IF NOT EXISTS `invoice`(
    invoiceId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    contractId VARCHAR(50) NOT NULL REFERENCES `contract`(contractId)
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
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ASUTOSH: DELIVERY TABLE
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
        'in-transit',
        'delivered',
        'failed',
        'cancelled'
    ) DEFAULT 'scheduled',
    trackingNumber VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (invoiceId) REFERENCES invoice(invoiceId)
);

-- ASUTOSH: PAYMENT TABLE
CREATE TABLE IF NOT EXISTS `payment`(
    paymentId CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    invoiceId CHAR(36) NOT NULL REFERENCES `invoice`(invoiceId) 
	ON DELETE CASCADE
	ON UPDATE CASCADE,
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
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);