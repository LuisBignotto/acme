CREATE DATABASE IF NOT EXISTS `acme-airlines`

use `acme-airlines`

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `baggage_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cpf` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7kqluf7wl0oxs7n90fpya03ss` (`cpf`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `addresses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `complement` varchar(255) DEFAULT NULL,
  `neighborhood` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKp624vxq0vboah4lfpj88fq8gt` (`user_id`),
  CONSTRAINT `FK1fa36y2oqhao3wgg2rw1pi459` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `baggages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `color` varchar(50) NOT NULL,
  `flight_id` bigint(20) NOT NULL,
  `last_location` varchar(100) NOT NULL,
  `tag` varchar(10) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `weight` decimal(38,2) NOT NULL,
  `status_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtgi6tk8o7q5j9o0cytnnyvvpf` (`status_id`),
  CONSTRAINT `FKtgi6tk8o7q5j9o0cytnnyvvpf` FOREIGN KEY (`status_id`) REFERENCES `baggage_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `baggage_trackers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tracker_user_id` bigint(20) DEFAULT NULL,
  `baggage_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbto0gkvywup0tmcs1asmxewho` (`baggage_id`),
  CONSTRAINT `FKbto0gkvywup0tmcs1asmxewho` FOREIGN KEY (`baggage_id`) REFERENCES `baggages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `flights` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `airplane_model` varchar(255) NOT NULL,
  `arrival_airport` varchar(255) NOT NULL,
  `arrival_date` datetime(6) NOT NULL,
  `departure_airport` varchar(255) NOT NULL,
  `departure_date` datetime(6) NOT NULL,
  `status` varchar(255) NOT NULL,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` text NOT NULL,
  `sender_id` bigint(20) NOT NULL,
  `ticket_id` bigint(20) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6iv985o3ybdk63srj731en4ba` (`ticket_id`),
  CONSTRAINT `FK6iv985o3ybdk63srj731en4ba` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `tickets` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

commit;