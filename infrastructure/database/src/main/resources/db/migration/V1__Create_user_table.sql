CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

-- Create index on email for faster lookups
CREATE INDEX idx_users_email ON users(email);

-- Create index on active status for filtering
CREATE INDEX idx_users_active ON users(active);

-- Create index on names for search functionality
CREATE INDEX idx_users_names ON users(first_name, last_name);

-- Insert some sample data (optional - for development/testing)
INSERT INTO users (id, email, first_name, last_name, active) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'john.doe@example.com', 'John', 'Doe', true),
('550e8400-e29b-41d4-a716-446655440002', 'jane.smith@example.com', 'Jane', 'Smith', true),
('550e8400-e29b-41d4-a716-446655440003', 'bob.johnson@example.com', 'Bob', 'Johnson', false);