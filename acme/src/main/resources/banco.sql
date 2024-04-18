CREATE DATABASE acme;

USE acme;

CREATE TABLE users (
                       id VARCHAR(36) PRIMARY KEY NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       cpf VARCHAR(14) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(255),
                       zipcode VARCHAR(255),
                       state VARCHAR(255),
                       city VARCHAR(255),
                       street VARCHAR(255),
                       neighborhood VARCHAR(255),
                       number VARCHAR(255),
                       complement VARCHAR(255),
                       active BOOLEAN NOT NULL,
                       role VARCHAR(255) NOT NULL
);

CREATE TABLE flights (
                         id VARCHAR(36) PRIMARY KEY NOT NULL,
                         flight_number VARCHAR(255) NOT NULL,
                         departure_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         arrival_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         departure_airport VARCHAR(255) NOT NULL,
                         arrival_airport VARCHAR(255) NOT NULL
);

CREATE TABLE baggages (
                          id VARCHAR(36) PRIMARY KEY NOT NULL,
                          user_id VARCHAR(36) NOT NULL,
                          tag VARCHAR(255) NOT NULL,
                          color VARCHAR(255) NOT NULL,
                          weight DOUBLE NOT NULL,
                          status VARCHAR(255) NOT NULL,
                          last_seen_location VARCHAR(255) NOT NULL,
                          flight_id VARCHAR(36) NOT NULL,
                          FOREIGN KEY (flight_id) REFERENCES flights (id),
                          FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE deleted_flights (
                          id VARCHAR(36) PRIMARY KEY NOT NULL,
                          flight_number VARCHAR(255) NOT NULL,
                          departure_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          arrival_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          departure_airport VARCHAR(255) NOT NULL,
                          arrival_airport VARCHAR(255) NOT NULL,
                          deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

SET GLOBAL event_scheduler = ON;

DELIMITER $$
CREATE PROCEDURE archive_past_flights()
BEGIN
    INSERT INTO deleted_flights (id, flight_number, departure_date, arrival_date, departure_airport, arrival_airport, deleted_at)
    SELECT id, flight_number, departure_date, arrival_date, departure_airport, arrival_airport, CURRENT_TIMESTAMP
    FROM flights
    WHERE arrival_date < CURRENT_TIMESTAMP;

    DELETE FROM flights
    WHERE arrival_date < CURRENT_TIMESTAMP;
END$$
DELIMITER ;

CREATE EVENT move_expired_flights_event
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP + INTERVAL 1 DAY
DO
    CALL archive_past_flights();
