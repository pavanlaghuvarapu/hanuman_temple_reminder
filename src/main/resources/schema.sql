-- Temple Puja Reminder System - PostgreSQL Schema
-- Run this script to initialize the database

-- Create Database (run as postgres superuser)
-- CREATE DATABASE temple_reminder;

-- Connect to temple_reminder database before running below

-- Drop tables if exist (for clean setup)
DROP TABLE IF EXISTS reminder_templates CASCADE;
DROP TABLE IF EXISTS couples CASCADE;

-- Table: couples
CREATE TABLE couples (
    id BIGSERIAL PRIMARY KEY,
    husband_name VARCHAR(100) NOT NULL,
    wife_name VARCHAR(100) NOT NULL,
    mobile_number VARCHAR(15) NOT NULL,
    week_number INTEGER NOT NULL CHECK (week_number >= 1 AND week_number <= 52),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_mobile UNIQUE (mobile_number)
);

-- Table: reminder_templates
CREATE TABLE reminder_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL,
    message_content TEXT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster week number lookup
CREATE INDEX idx_couples_week_number ON couples(week_number);

-- Index for search
CREATE INDEX idx_couples_husband_name ON couples(husband_name);
CREATE INDEX idx_couples_wife_name ON couples(wife_name);

-- Insert default reminder template
INSERT INTO reminder_templates (template_name, message_content, updated_at)
VALUES (
    'Default Puja Reminder',
    'Jai Shri Ram! 🙏

Dear {HUSBAND_NAME} ji and {WIFE_NAME} ji,

This is a respectful reminder from Hanuman Temple.

You are scheduled to perform the special Tuesday Puja on:
📅 Date: {PUJA_DATE}
🔢 Week Number: {WEEK_NUMBER}

Please arrive at the temple by 6:00 AM for the Puja preparations.

For any queries, please contact the temple administration.

Jai Bajrang Bali! 🚩

- Temple Administration',
    CURRENT_TIMESTAMP
);

-- Sample data (optional - uncomment to insert test couples)
/*
INSERT INTO couples (husband_name, wife_name, mobile_number, week_number) VALUES
('Ramesh Kumar', 'Sunita Kumar', '9876543210', 1),
('Suresh Sharma', 'Kavita Sharma', '9876543211', 1),
('Mahesh Gupta', 'Priya Gupta', '9876543212', 2),
('Dinesh Verma', 'Anita Verma', '9876543213', 2),
('Naresh Singh', 'Meena Singh', '9876543214', 3),
('Rajesh Patel', 'Neha Patel', '9876543215', 3);
*/
