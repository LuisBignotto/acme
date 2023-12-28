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

CREATE TABLE flight (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                        flightNumber VARCHAR(255) NOT NULL,
                        departureDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        arrivalDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        departureAirport VARCHAR(255) NOT NULL,
                        arrivalAirport VARCHAR(255) NOT NULL
);

CREATE TABLE baggage (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                         userId BIGINT NOT NULL,
                         tag VARCHAR(255) NOT NULL,
                         color VARCHAR(255) NOT NULL,
                         weight DOUBLE NOT NULL,
                         dimension VARCHAR(255),
                         status VARCHAR(255) NOT NULL,
                         lastSeenLocation VARCHAR(255) NOT NULL,
                         flight BIGINT NOT NULL,
                         FOREIGN KEY (flight) REFERENCES flight (id),
                         FOREIGN KEY (userId) REFERENCES users (id)
);
