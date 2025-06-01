-- Alter all tables to use BIGINT for id columns instead of INT
-- This is needed to match the Long type used in Java entity classes

-- First, drop all foreign key constraints
ALTER TABLE ticket DROP CONSTRAINT ticket_match_id_fkey;
ALTER TABLE match DROP CONSTRAINT match_stadium_id_fkey;
ALTER TABLE user_roles DROP CONSTRAINT user_roles_user_id_fkey;
ALTER TABLE user_roles DROP CONSTRAINT user_roles_role_id_fkey;

-- Update stadium table
CREATE SEQUENCE stadium_id_seq_bigint;
ALTER TABLE stadium ALTER COLUMN id TYPE BIGINT;
ALTER TABLE stadium ALTER COLUMN id SET DEFAULT nextval('stadium_id_seq_bigint');
SELECT setval('stadium_id_seq_bigint', (SELECT max(id) FROM stadium));
ALTER SEQUENCE stadium_id_seq_bigint OWNED BY stadium.id;

-- Update match table
CREATE SEQUENCE match_id_seq_bigint;
ALTER TABLE match ALTER COLUMN id TYPE BIGINT;
ALTER TABLE match ALTER COLUMN id SET DEFAULT nextval('match_id_seq_bigint');
SELECT setval('match_id_seq_bigint', (SELECT max(id) FROM match));
ALTER SEQUENCE match_id_seq_bigint OWNED BY match.id;
ALTER TABLE match ALTER COLUMN stadium_id TYPE BIGINT;

-- Update ticket table
CREATE SEQUENCE ticket_id_seq_bigint;
ALTER TABLE ticket ALTER COLUMN id TYPE BIGINT;
ALTER TABLE ticket ALTER COLUMN id SET DEFAULT nextval('ticket_id_seq_bigint');
SELECT setval('ticket_id_seq_bigint', (SELECT max(id) FROM ticket));
ALTER SEQUENCE ticket_id_seq_bigint OWNED BY ticket.id;
ALTER TABLE ticket ALTER COLUMN match_id TYPE BIGINT;

-- Update users table
CREATE SEQUENCE users_id_seq_bigint;
ALTER TABLE users ALTER COLUMN id TYPE BIGINT;
ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq_bigint');
SELECT setval('users_id_seq_bigint', (SELECT max(id) FROM users));
ALTER SEQUENCE users_id_seq_bigint OWNED BY users.id;

-- Update roles table
CREATE SEQUENCE roles_id_seq_bigint;
ALTER TABLE roles ALTER COLUMN id TYPE BIGINT;
ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq_bigint');
SELECT setval('roles_id_seq_bigint', (SELECT max(id) FROM roles));
ALTER SEQUENCE roles_id_seq_bigint OWNED BY roles.id;

-- Update user_roles junction table
ALTER TABLE user_roles ALTER COLUMN user_id TYPE BIGINT;
ALTER TABLE user_roles ALTER COLUMN role_id TYPE BIGINT;

-- Recreate all foreign key constraints
ALTER TABLE match ADD CONSTRAINT match_stadium_id_fkey FOREIGN KEY (stadium_id) REFERENCES stadium(id) ON DELETE RESTRICT;
ALTER TABLE ticket ADD CONSTRAINT ticket_match_id_fkey FOREIGN KEY (match_id) REFERENCES match(id) ON DELETE CASCADE;
ALTER TABLE user_roles ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE user_roles ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;
