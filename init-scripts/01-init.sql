-- Initial database setup
-- This script runs when the PostgreSQL container starts for the first time

-- Create the database if it doesn't exist (though it's already created via POSTGRES_DB)
-- This is just for reference

-- Grant necessary permissions
GRANT ALL PRIVILEGES ON DATABASE fullstack TO postgres;

-- The actual tables will be created by Flyway migrations
