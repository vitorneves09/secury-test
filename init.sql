-- Initialize blog database
USE blogdb;

-- Create users table if not exists (optional, as JPA will create it)
-- This is just an example for initial setup

SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- Grant privileges to blog user
GRANT ALL PRIVILEGES ON blogdb.* TO 'bloguser'@'%';
FLUSH PRIVILEGES;

-- Optional: Insert initial data
-- INSERT INTO users (username, email, password, role) VALUES ('admin', 'admin@example.com', 'hashed_password', 'ADMIN');
