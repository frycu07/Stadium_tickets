-- Alter users table to make password nullable
ALTER TABLE users ALTER COLUMN password DROP NOT NULL;