-- =====================================================
-- Migration: V1 - Create Accounts Schema
-- Description: Initial schema for Account Service
-- Author: Financer Team
-- Date: 2025-11-07
-- =====================================================

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS financer;

-- Set search path
SET search_path TO financer;

-- =====================================================
-- Table: accounts
-- Description: Main table for financial accounts
-- =====================================================
CREATE TABLE IF NOT EXISTS accounts (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_name        VARCHAR(255) NOT NULL,
    account_type        VARCHAR(50) NOT NULL CHECK (account_type IN ('CHECKING', 'SAVINGS', 'CREDIT_CARD', 'INVESTMENT', 'CASH')),
    bank_name           VARCHAR(255),
    bank_code           VARCHAR(10),
    account_number      VARCHAR(50),
    agency              VARCHAR(20),
    balance             DECIMAL(19, 4) DEFAULT 0.00 NOT NULL,
    currency            VARCHAR(3) DEFAULT 'BRL' NOT NULL,
    is_active           BOOLEAN DEFAULT TRUE NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             INTEGER DEFAULT 0 NOT NULL,
    
    -- Constraints
    CONSTRAINT uk_account_number UNIQUE (bank_code, account_number, agency)
);

-- =====================================================
-- Table: account_audit
-- Description: Audit trail for account changes
-- =====================================================
CREATE TABLE IF NOT EXISTS account_audit (
    audit_id            BIGSERIAL PRIMARY KEY,
    account_id          UUID NOT NULL,
    operation           VARCHAR(20) NOT NULL CHECK (operation IN ('CREATE', 'UPDATE', 'DELETE', 'ACTIVATE', 'DEACTIVATE')),
    old_value           JSONB,
    new_value           JSONB,
    changed_by          VARCHAR(100),
    changed_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ip_address          VARCHAR(45),
    user_agent          TEXT,
    
    -- Foreign Key
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- =====================================================
-- Indexes for Performance
-- =====================================================

-- Accounts indexes
CREATE INDEX idx_accounts_type ON accounts(account_type);
CREATE INDEX idx_accounts_active ON accounts(is_active);
CREATE INDEX idx_accounts_bank ON accounts(bank_code);
CREATE INDEX idx_accounts_created_at ON accounts(created_at DESC);

-- Audit indexes
CREATE INDEX idx_audit_account_id ON account_audit(account_id);
CREATE INDEX idx_audit_operation ON account_audit(operation);
CREATE INDEX idx_audit_changed_at ON account_audit(changed_at DESC);
CREATE INDEX idx_audit_changed_by ON account_audit(changed_by);

-- =====================================================
-- Function: Update timestamp automatically
-- =====================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    NEW.version = OLD.version + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- Trigger: Auto-update timestamp on accounts
-- =====================================================
CREATE TRIGGER trg_accounts_updated_at
    BEFORE UPDATE ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- Function: Audit trail trigger
-- =====================================================
CREATE OR REPLACE FUNCTION audit_account_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO account_audit (account_id, operation, old_value, changed_by)
        VALUES (OLD.id, 'DELETE', to_jsonb(OLD), current_user);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO account_audit (account_id, operation, old_value, new_value, changed_by)
        VALUES (NEW.id, 'UPDATE', to_jsonb(OLD), to_jsonb(NEW), current_user);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO account_audit (account_id, operation, new_value, changed_by)
        VALUES (NEW.id, 'CREATE', to_jsonb(NEW), current_user);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- Trigger: Audit all account changes
-- =====================================================
CREATE TRIGGER trg_accounts_audit
    AFTER INSERT OR UPDATE OR DELETE ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION audit_account_changes();

-- =====================================================
-- Comments for Documentation
-- =====================================================
COMMENT ON TABLE accounts IS 'Financial accounts managed by the system';
COMMENT ON COLUMN accounts.id IS 'Unique identifier (UUID)';
COMMENT ON COLUMN accounts.account_type IS 'Type of account: CHECKING, SAVINGS, CREDIT_CARD, INVESTMENT, CASH';
COMMENT ON COLUMN accounts.balance IS 'Current balance in the specified currency';
COMMENT ON COLUMN accounts.is_active IS 'Soft delete flag - false means deleted';
COMMENT ON COLUMN accounts.version IS 'Optimistic locking version number';

COMMENT ON TABLE account_audit IS 'Complete audit trail of all account changes';
COMMENT ON COLUMN account_audit.old_value IS 'JSON snapshot of data before change';
COMMENT ON COLUMN account_audit.new_value IS 'JSON snapshot of data after change';
