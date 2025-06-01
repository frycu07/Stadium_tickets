-- Insert sample users (admin password is 'admin', user password is 'user' encoded with BCrypt)
INSERT INTO users (username, password, email, created_at) VALUES
('admin', '$2a$10$hvAk6fgcdI6XDfUBl7h6dO01qzZQ2v9S98N5cohpoK9N6MC8Qrp/6', 'admin@example.com', CURRENT_TIMESTAMP),
('user', '$2a$10$2q52UTpSyX/oAnRm9y4Yp.vs8F6yaSpaQMAWJrxyiYcG2nNHx1fIK', 'user@example.com', CURRENT_TIMESTAMP);

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 2), -- admin has ADMIN role
(1, 1), -- admin also has USER role
(2, 1); -- user has USER role

-- Insert sample stadiums
INSERT INTO stadium (name, city, capacity) VALUES 
('National Stadium', 'Warsaw', 58000),
('Wembley Stadium', 'London', 90000),
('Camp Nou', 'Barcelona', 99000);

-- Insert sample matches
INSERT INTO match (home_team, away_team, match_date, stadium_id) VALUES 
('Poland', 'Germany', '2023-06-15 18:00:00', 1),
('England', 'France', '2023-06-20 20:00:00', 2),
('Barcelona', 'Real Madrid', '2023-06-25 21:00:00', 3);

-- Insert sample tickets for Poland vs Germany match
INSERT INTO ticket (match_id, seat_row, seat_number, price, status) VALUES 
(1, 'A', '1', 100.00, 'FREE'),
(1, 'A', '2', 100.00, 'FREE'),
(1, 'A', '3', 100.00, 'FREE'),
(1, 'B', '1', 80.00, 'FREE'),
(1, 'B', '2', 80.00, 'FREE'),
(1, 'B', '3', 80.00, 'FREE');

-- Insert sample tickets for England vs France match
INSERT INTO ticket (match_id, seat_row, seat_number, price, status) VALUES 
(2, 'A', '1', 120.00, 'FREE'),
(2, 'A', '2', 120.00, 'FREE'),
(2, 'A', '3', 120.00, 'FREE'),
(2, 'B', '1', 100.00, 'FREE'),
(2, 'B', '2', 100.00, 'FREE'),
(2, 'B', '3', 100.00, 'FREE');

-- Insert sample tickets for Barcelona vs Real Madrid match
INSERT INTO ticket (match_id, seat_row, seat_number, price, status) VALUES 
(3, 'A', '1', 150.00, 'FREE'),
(3, 'A', '2', 150.00, 'FREE'),
(3, 'A', '3', 150.00, 'FREE'),
(3, 'B', '1', 130.00, 'FREE'),
(3, 'B', '2', 130.00, 'FREE'),
(3, 'B', '3', 130.00, 'FREE');
