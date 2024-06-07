DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'ims_user') THEN
      CREATE ROLE ims_user LOGIN PASSWORD 'ims_password';
   END IF;
END
$$;

-- Create database if it doesn't exist
CREATE DATABASE ims_dev OWNER ims_user;

-- Connect to the database and create tables
\connect ims_dev;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com');
INSERT INTO users (name, email) VALUES ('Bob', 'bob@example.com');




