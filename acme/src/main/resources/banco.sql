CREATE DATABASE acme;

USE acme;

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(255),
                       zipcode VARCHAR(255),
                       state VARCHAR(255),
                       city VARCHAR(255),
                       street VARCHAR(255),
                       neighborhood VARCHAR(255),
                       number VARCHAR(255),
                       complement VARCHAR(255),
                       active BOOLEAN NOT NULL DEFAULT false,
                       role VARCHAR(255) NOT NULL
);

CREATE TABLE flights (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                         flight_number VARCHAR(255) NOT NULL,
                         departure_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         arrival_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         departure_airport VARCHAR(255) NOT NULL,
                         arrival_airport VARCHAR(255) NOT NULL
);

CREATE TABLE baggages (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                          user_id BIGINT NOT NULL,
                          tag VARCHAR(255) NOT NULL,
                          color VARCHAR(255) NOT NULL,
                          weight DOUBLE NOT NULL,
                          status VARCHAR(255) NOT NULL,
                          last_seen_location VARCHAR(255) NOT NULL,
                          flight_id BIGINT NOT NULL,
                          FOREIGN KEY (flight_id) REFERENCES flights (id),
                          FOREIGN KEY (user_id) REFERENCES users (id)
);