-- =====================================================
-- Seed Data: Sample Accounts
-- Description: Initial data for testing and development
-- Author: Financer Team
-- Date: 2025-11-07
-- =====================================================

SET search_path TO financer;

-- =====================================================
-- Insert sample checking accounts
-- =====================================================
INSERT INTO accounts (id, account_name, account_type, bank_name, bank_code, account_number, agency, balance, created_by)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'Conta Corrente Principal', 'CHECKING', 'Banco do Brasil', '001', '123456-7', '0001', 5000.00, 'SYSTEM'),
    ('22222222-2222-2222-2222-222222222222', 'Conta Poupança', 'SAVINGS', 'Caixa Econômica', '104', '789012-3', '0002', 10000.00, 'SYSTEM'),
    ('33333333-3333-3333-3333-333333333333', 'Investimentos', 'INVESTMENT', 'Itaú', '341', '456789-0', '0003', 50000.00, 'SYSTEM')
ON CONFLICT DO NOTHING;

-- =====================================================
-- Insert sample credit cards
-- =====================================================
INSERT INTO cards (id, account_id, card_number_masked, card_holder_name, card_type, card_brand, expiry_date, credit_limit, available_limit, due_day, closing_day, created_by)
VALUES 
    ('44444444-4444-4444-4444-444444444444', '11111111-1111-1111-1111-111111111111', '**** 1234', 'João da Silva', 'CREDIT', 'VISA', '2027-12-31', 10000.00, 8500.00, 10, 5, 'SYSTEM'),
    ('55555555-5555-5555-5555-555555555555', '22222222-2222-2222-2222-222222222222', '**** 5678', 'Maria Santos', 'DEBIT', 'MASTERCARD', '2026-06-30', NULL, NULL, NULL, NULL, 'SYSTEM')
ON CONFLICT DO NOTHING;

-- =====================================================
-- Insert sample transactions
-- =====================================================
INSERT INTO transactions (id, account_id, card_id, transaction_type, category, description, amount, transaction_date, status, created_by)
VALUES 
    ('66666666-6666-6666-6666-666666666666', '11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 'CREDIT_CARD', 'Alimentação', 'Supermercado XYZ', -250.50, CURRENT_TIMESTAMP - INTERVAL '5 days', 'COMPLETED', 'SYSTEM'),
    ('77777777-7777-7777-7777-777777777777', '11111111-1111-1111-1111-111111111111', NULL, 'PIX', 'Transferência', 'Pagamento aluguel', -1500.00, CURRENT_TIMESTAMP - INTERVAL '3 days', 'COMPLETED', 'SYSTEM'),
    ('88888888-8888-8888-8888-888888888888', '22222222-2222-2222-2222-222222222222', NULL, 'DEPOSIT', 'Salário', 'Depósito mensal', 5000.00, CURRENT_TIMESTAMP - INTERVAL '1 day', 'COMPLETED', 'SYSTEM')
ON CONFLICT DO NOTHING;

-- =====================================================
-- Verify inserted data
-- =====================================================
SELECT 
    'Accounts' as table_name, 
    COUNT(*) as record_count 
FROM accounts
UNION ALL
SELECT 
    'Cards' as table_name, 
    COUNT(*) as record_count 
FROM cards
UNION ALL
SELECT 
    'Transactions' as table_name, 
    COUNT(*) as record_count 
FROM transactions;
