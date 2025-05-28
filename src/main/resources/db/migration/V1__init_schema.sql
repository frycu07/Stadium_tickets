-- Create stadium table
CREATE TABLE stadium (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    capacity INT NOT NULL
);

-- Create match table
CREATE TABLE match (
    id SERIAL PRIMARY KEY,
    home_team VARCHAR(100) NOT NULL,
    away_team VARCHAR(100) NOT NULL,
    match_date TIMESTAMP NOT NULL,
    stadium_id INT NOT NULL REFERENCES stadium(id) ON DELETE RESTRICT
);

-- Create ticket table
CREATE TABLE ticket (
    id SERIAL PRIMARY KEY,
    match_id INT NOT NULL REFERENCES match(id) ON DELETE CASCADE,
    seat_row VARCHAR(5),
    seat_number VARCHAR(5),
    price NUMERIC(8,2) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'FREE'
);
