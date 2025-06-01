-- Add passwordless admin and user accounts
INSERT INTO users (username, email, created_at)
VALUES ('admin_passwordless', 'admin_passwordless@example.com', CURRENT_TIMESTAMP);

INSERT INTO users (username, email, created_at)
VALUES ('user_passwordless', 'user_passwordless@example.com', CURRENT_TIMESTAMP);

-- Get the IDs of the newly created users
DO $$
DECLARE
    admin_id INTEGER;
    user_id INTEGER;
    admin_role_id INTEGER;
    user_role_id INTEGER;
BEGIN
    SELECT id INTO admin_id FROM users WHERE username = 'admin_passwordless';
    SELECT id INTO user_id FROM users WHERE username = 'user_passwordless';
    SELECT id INTO admin_role_id FROM roles WHERE name = 'ADMIN';
    SELECT id INTO user_role_id FROM roles WHERE name = 'USER';

    -- Assign roles to users
    INSERT INTO user_roles (user_id, role_id) VALUES (admin_id, admin_role_id);
    INSERT INTO user_roles (user_id, role_id) VALUES (admin_id, user_role_id);
    INSERT INTO user_roles (user_id, role_id) VALUES (user_id, user_role_id);
END $$;