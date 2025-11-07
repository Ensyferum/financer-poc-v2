-- =====================================================
-- Migration: V2 - Create Cards and Transactions Schema
-- Description: Schema for credit/debit cards and transactions
-- Author: Financer Team
-- Date: 2025-11-07
-- =====================================================

SET search_path TO financer;

-- =====================================================
-- Table: cards
-- Description: Credit and debit cards management
-- =====================================================
CREATE TABLE IF NOT EXISTS cards (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id          UUID NOT NULL,
    card_number_masked  VARCHAR(20) NOT NULL, -- Last 4 digits only
    card_holder_name    VARCHAR(255) NOT NULL,
    card_type           VARCHAR(20) NOT NULL CHECK (card_type IN ('CREDIT', 'DEBIT', 'PREPAID')),
    card_brand          VARCHAR(50) NOT NULL CHECK (card_brand IN ('VISA', 'MASTERCARD', 'ELO', 'AMEX', 'HIPERCARD', 'OTHER')),
    expiry_date         DATE NOT NULL,
    credit_limit        DECIMAL(19, 4),
    available_limit     DECIMAL(19, 4),
    due_day             INTEGER CHECK (due_day BETWEEN 1 AND 31),
    closing_day         INTEGER CHECK (closing_day BETWEEN 1 AND 31),
    is_active           BOOLEAN DEFAULT TRUE NOT NULL,
    is_blocked          BOOLEAN DEFAULT FALSE NOT NULL,
    block_reason        TEXT,
    created_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             INTEGER DEFAULT 0 NOT NULL,
    
    -- Foreign Key
    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- =====================================================
-- Table: transactions
-- Description: Financial transactions (PIX, TED, DOC, etc)
-- =====================================================
CREATE TABLE IF NOT EXISTS transactions (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id          UUID NOT NULL,
    card_id             UUID,
    transaction_type    VARCHAR(50) NOT NULL CHECK (transaction_type IN 
                        ('PIX', 'TED', 'DOC', 'CREDIT_CARD', 'DEBIT_CARD', 
                         'BANK_SLIP', 'TRANSFER', 'WITHDRAWAL', 'DEPOSIT', 'PAYMENT')),
    category            VARCHAR(100),
    description         TEXT NOT NULL,
    amount              DECIMAL(19, 4) NOT NULL,
    currency            VARCHAR(3) DEFAULT 'BRL' NOT NULL,
    transaction_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    due_date            DATE,
    is_recurring        BOOLEAN DEFAULT FALSE NOT NULL,
    recurrence_pattern  VARCHAR(50) CHECK (recurrence_pattern IN 
                        ('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY', NULL)),
    installments        INTEGER DEFAULT 1,
    installment_number  INTEGER DEFAULT 1,
    parent_transaction_id UUID,
    status              VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN 
                        ('PENDING', 'COMPLETED', 'CANCELLED', 'FAILED')),
    notes               TEXT,
    created_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             INTEGER DEFAULT 0 NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_card FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE SET NULL,
    CONSTRAINT fk_parent_transaction FOREIGN KEY (parent_transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    
    -- Business Rules
    CONSTRAINT chk_installment_valid CHECK (installment_number <= installments)
);

-- =====================================================
-- Table: transaction_audit
-- Description: Audit trail for transactions
-- =====================================================
CREATE TABLE IF NOT EXISTS transaction_audit (
    audit_id            BIGSERIAL PRIMARY KEY,
    transaction_id      UUID NOT NULL,
    operation           VARCHAR(20) NOT NULL CHECK (operation IN ('CREATE', 'UPDATE', 'DELETE', 'CANCEL')),
    old_value           JSONB,
    new_value           JSONB,
    changed_by          VARCHAR(100),
    changed_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ip_address          VARCHAR(45),
    
    -- Foreign Key
    CONSTRAINT fk_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

-- =====================================================
-- Indexes for Performance
-- =====================================================

-- Cards indexes
CREATE INDEX idx_cards_account ON cards(account_id);
CREATE INDEX idx_cards_type ON cards(card_type);
CREATE INDEX idx_cards_active ON cards(is_active);
CREATE INDEX idx_cards_expiry ON cards(expiry_date);

-- Transactions indexes
CREATE INDEX idx_transactions_account ON transactions(account_id);
CREATE INDEX idx_transactions_card ON transactions(card_id);
CREATE INDEX idx_transactions_type ON transactions(transaction_type);
CREATE INDEX idx_transactions_category ON transactions(category);
CREATE INDEX idx_transactions_date ON transactions(transaction_date DESC);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_recurring ON transactions(is_recurring);
CREATE INDEX idx_transactions_parent ON transactions(parent_transaction_id);

-- Transaction audit indexes
CREATE INDEX idx_tx_audit_transaction ON transaction_audit(transaction_id);
CREATE INDEX idx_tx_audit_changed_at ON transaction_audit(changed_at DESC);

-- =====================================================
-- Triggers: Auto-update timestamps
-- =====================================================
CREATE TRIGGER trg_cards_updated_at
    BEFORE UPDATE ON cards
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_transactions_updated_at
    BEFORE UPDATE ON transactions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- Function: Transaction audit trigger
-- =====================================================
CREATE OR REPLACE FUNCTION audit_transaction_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO transaction_audit (transaction_id, operation, old_value, changed_by)
        VALUES (OLD.id, 'DELETE', to_jsonb(OLD), current_user);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO transaction_audit (transaction_id, operation, old_value, new_value, changed_by)
        VALUES (NEW.id, 'UPDATE', to_jsonb(OLD), to_jsonb(NEW), current_user);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO transaction_audit (transaction_id, operation, new_value, changed_by)
        VALUES (NEW.id, 'CREATE', to_jsonb(NEW), current_user);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- Trigger: Audit all transaction changes
-- =====================================================
CREATE TRIGGER trg_transactions_audit
    AFTER INSERT OR UPDATE OR DELETE ON transactions
    FOR EACH ROW
    EXECUTE FUNCTION audit_transaction_changes();

-- =====================================================
-- Comments for Documentation
-- =====================================================
COMMENT ON TABLE cards IS 'Credit and debit cards linked to accounts';
COMMENT ON COLUMN cards.card_number_masked IS 'Only last 4 digits stored for security';
COMMENT ON COLUMN cards.credit_limit IS 'Total credit limit (for credit cards)';
COMMENT ON COLUMN cards.available_limit IS 'Available credit after transactions';

COMMENT ON TABLE transactions IS 'All financial transactions';
COMMENT ON COLUMN transactions.is_recurring IS 'True for recurring payments';
COMMENT ON COLUMN transactions.installments IS 'Number of installments for split payments';
COMMENT ON COLUMN transactions.parent_transaction_id IS 'Links to parent for installment payments';
