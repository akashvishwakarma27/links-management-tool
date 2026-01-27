-- Initial data for H2 database
-- This will be automatically executed when the application starts

-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, role, created_at, updated_at) 
VALUES 
    ('admin', 'admin@smartlinkfinder.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('akash_super_admin', 'akash.super@admin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'SUPER_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample links
INSERT INTO links (reference_code, full_url, description, brand_name, status, created_at, updated_at) 
VALUES 
    ('PI-31001', 'https://assets.company.com/pharma/egypt/singrix/pi_31001_v1.pdf', 'Singrix Product Information - Egypt', 'Singrix', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PI-32001', 'https://assets.company.com/pharma/india/singrix/pi_32001_v1.pdf', 'Singrix Product Information - India', 'Singrix', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PI-33001', 'https://assets.company.com/pharma/pakistan/singrix/pi_33001_v1.pdf', 'Singrix Product Information - Pakistan', 'Singrix', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PI-34001', 'https://assets.company.com/pharma/gulf/ventolin/pi_34001_v1.pdf', 'Ventolin Product Information - Gulf', 'Ventolin', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PI-35001', 'https://assets.company.com/pharma/saudi/panadol/pi_35001_v1.pdf', 'Panadol Product Information - Saudi', 'Panadol', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
