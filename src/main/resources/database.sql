-- Adminer 4.8.1 MySQL 10.6.12-MariaDB-0ubuntu0.22.04.1 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `service`;
CREATE DATABASE `service` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `service`;

DROP TABLE IF EXISTS `collaborators`;
CREATE TABLE `collaborators` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `collaborators_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `collaborators_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `collaborators`;

DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  `task_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FKi7pp0331nbiwd2844kg78kfwb` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `comments`;

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `about` varchar(258) DEFAULT NULL,
  `task_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `finish_date` datetime DEFAULT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner_id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `orders_ibfk_4` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `orders`;

DELIMITER ;;

CREATE TRIGGER `order_deleted` AFTER DELETE ON `orders` FOR EACH ROW
BEGIN
    DELETE FROM `tasks` WHERE user_id = OLD.id;
END;;

DELIMITER ;

DROP TABLE IF EXISTS `pages`;
CREATE TABLE `pages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ivp` varchar(32) NOT NULL,
  `token` varchar(128) NOT NULL,
  `secret` varchar(386) NOT NULL,
  `padding` varchar(88) NOT NULL,
  `created` datetime(6) NOT NULL,
  `expires` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `pages`;
INSERT INTO `pages` (`id`, `ivp`, `token`, `secret`, `padding`, `created`, `expires`) VALUES
(34,	'sakkjdWhWFp8hnl7aaSOmQ==',	'075c270cd7c0cef078aed26179c3a381d7b4c002037a9356b4de79b63967a48ff21c94e588e0f02f4230188b3c2b37c6dfc9fd91020987b7b10c09b53f90163f',	'2c478d12af37d4f7f905964e91f6c940e9bddf048d36e079314b734e50bf8a64d69a7e62349450e24936694c6e35e89f2729c4222b76731983e7defce2cfa3c9:nkTG1u1/yUKFYsdMg+a84LLKoOWn7RHeeK1oTYC3fQGnh0LQHBgrlhJ2X5hVSQpd7RtUNZChQfo8kSQUgbn1upvwJux7lSUzzdzceHp5xFUdi2IkqGZGSAgul8Z+HXHLMDzKYub4CPsJcQba+Wb4cqtaFIwuNi1POB+jB0DaMlQ=',	'5MmFTA2gDFY1T0XRrHaX76DxcBD7qkZqhT5sBG6TzBg=',	'2023-08-31 22:08:42.000000',	'2023-08-31 22:40:42.000000');

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` enum('ADMIN','MODER','USER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `roles`;

DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner_id`),
  CONSTRAINT `tasks_ibfk_2` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=545370 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `tasks`;
INSERT INTO `tasks` (`id`, `title`, `owner_id`, `created`, `updated`) VALUES
(545342,	'Test 1',	1,	'2023-08-31 14:19:47.000000',	'2023-08-31 14:19:47.000000'),
(545343,	'Test 2',	1,	'2020-02-10 14:20:09.000000',	'2022-08-31 14:20:09.000000'),
(545344,	'Test 3',	1,	'2020-08-31 14:20:09.000000',	'2022-01-20 14:20:09.000000'),
(545345,	'Test 4',	1,	'2023-08-31 14:22:19.000000',	'2023-08-31 14:22:19.000000'),
(545346,	'Test 5',	1,	'2023-07-31 14:19:47.000000',	'2023-08-31 14:19:47.000000'),
(545347,	'Test 6',	1,	'2020-02-10 14:20:09.000000',	'2023-08-31 14:20:09.000000'),
(545348,	'Test 7',	1,	'2020-08-31 14:20:09.000000',	'2022-01-20 14:20:09.000000'),
(545349,	'Test 8',	1,	'2023-08-31 14:22:19.000000',	'2023-08-31 14:22:19.000000'),
(545353,	'Test 9',	1,	'2021-01-03 14:19:47.000000',	'2021-08-31 14:19:47.000000'),
(545354,	'Test 10',	1,	'2020-02-10 14:20:09.000000',	'2023-08-31 14:20:09.000000'),
(545355,	'Test 11',	1,	'2020-08-31 14:20:09.000000',	'2022-01-20 14:20:09.000000'),
(545356,	'Test 12',	1,	'2021-05-20 14:22:19.000000',	'2021-08-31 14:22:19.000000'),
(545357,	'Test 13',	1,	'2023-08-31 14:19:47.000000',	'2023-08-31 14:19:47.000000'),
(545358,	'Test 14',	1,	'2020-02-10 14:20:09.000000',	'2021-08-31 14:20:09.000000'),
(545359,	'Test 15',	1,	'2020-08-31 14:20:09.000000',	'2022-01-20 14:20:09.000000'),
(545360,	'Test 16',	1,	'2023-08-31 14:22:19.000000',	'2023-08-31 14:22:19.000000'),
(545368,	'Test 17',	1,	'2023-08-31 18:26:40.000000',	'2023-08-31 18:26:40.000000'),
(545369,	'Test 18',	1,	'2023-08-31 20:41:49.000000',	'2023-08-31 20:41:49.000000');

DELIMITER ;;

CREATE TRIGGER `task_deleted` AFTER DELETE ON `tasks` FOR EACH ROW
BEGIN
    DELETE FROM `collaborators` WHERE task_id = OLD.id;
    DELETE FROM `orders` WHERE task_id = OLD.id;
END;;

DELIMITER ;

DROP TABLE IF EXISTS `tokens`;
CREATE TABLE `tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token` varchar(258) NOT NULL,
  `uuids` varchar(258) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `users` (`user_id`),
  CONSTRAINT `tokens_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `tokens`;
INSERT INTO `tokens` (`id`, `token`, `uuids`, `user_id`, `created`, `updated`) VALUES
(5,	'fef17f53ae47e0214d35de01b8fa7f8d234648360a6c90025a08a801a10b5df29a9cb200155d1d39eb0d9dfb49c83a48defebc6d346d6a6df7510f9df41105f9b22f711b31543ed7fb3baad52b70dfa35557799f98e3994a77a996a1f252bc6226e3abebf9fb37f4226c7284587ae23451bd51542f2d38136e8024ae69063c5a',	'559a05b8-4c59-40a9-bded-3ec018b4ea0a',	2,	'2023-08-31 15:34:51.000000',	'2023-08-31 15:34:51.000000'),
(6,	'b26a7b65e70395e6b1c688cd6e81a3e0d0d5192eaf088913f72866baf888c0e33ff166d4b9d00c58f9156d642f63efec1f4a28447e252a003b437f8d8c4e52a1db4bbd8c64651a99bc64d402928b42091dbc0e34e01841143d76934351aa89eee7a63003df9f96804570b34decfdf2b47d5a7df7bc2d5c34dc40526547e3afb4',	'e1187137-e33f-49d9-b1be-a39696d07ada',	1,	'2023-08-31 15:38:53.000000',	'2023-08-31 15:38:53.000000'),
(7,	'b775b515da0ebf925d462faecb4d5b8c0dc8291600ae13fb0deb561476bf1e1ea33ef8c407615cc46c8e5ef404ecfea90eac7e7fc558300274b320839a26916f54d0089ef54ced3cb7c9fa2fa0ab12107a5227a0c4373185b4e71203b0558d9a419f14ce1d45c0dca2628db33bd53d7d0500fd8e0c3723732d42f8a1d7c2883b',	'62de78d8-3663-42b6-a718-e5701e78aa2a',	2,	'2023-08-31 21:54:47.000000',	'2023-08-31 21:54:47.000000'),
(8,	'e6a4304a62c588570b069cc162dad3e2cc149d2955d2a9be2d4f404997d175afe7f4e1e10ba8c6183ba4984fca70cba130b765564e0e76d2c3733718f99cecc7bebbcd9b659e54de3b4d769eaa03fa7bdfcf635d2dc4cc94710689525cc310e97ab2b5edb4d8206979dedbda4d1b9b2f5f1571e4ec6d8009c85ed11f03916de2',	'2f73761b-9b18-492c-a494-966069533732',	1,	'2023-08-31 21:56:38.000000',	'2023-08-31 21:56:38.000000');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `usermail` varchar(120) NOT NULL,
  `password` varchar(386) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `usermail` (`usermail`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `users`;
INSERT INTO `users` (`id`, `fullname`, `username`, `usermail`, `password`, `created`, `updated`) VALUES
(1,	'Charset',	'charset',	'charset@charset.utf',	'10000:4343d2635c1552064c7d392d86f7f6278a48951c1c0f2d28ea5a0509cdb0d3033518b716571961804da599c890ab33ca185a38772d4e63974103cb14db15b23ebea7625000e0da89fafc5b4ff056a83f8354513d9d05b512fe4cd6bb49b3143470214bacedfe4893739e0e11082173a587b4ae9f73d6f5c56610eb341fef6f11:19b0c3ef45b6ce3920480d90e8a98cfcbb2c4529ad6cc9bc8c66f19805f50513',	'2023-08-28 14:50:37.000000',	'2023-08-28 14:50:37.000000'),
(2,	'Test',	'test',	'test@test.com',	'10000:4b842f3db8129e2b0c66209b2ec5f694ee7f1b596ff348e811775209ca23fd0305c262c920b5d889c4d94f0613841258fad01e5c498735014950dfbca50769016468f2a3a12393b5c45339b8c6390fda22d283431d7da446e2abc69b5e901aa4af89ea002fa84a8b3d4ed8e3dfb8ec02e6b5dee0b22f52c659ed9db0faf59f5d:9327a4e4237fde17f95658055ff13c5395fadcfdd758b2d0a3d5eb9e59fb09a7',	'2023-08-29 13:33:05.000000',	'2023-08-29 13:33:05.000000');

DELIMITER ;;

CREATE TRIGGER `user_deleted` AFTER DELETE ON `users` FOR EACH ROW
BEGIN
    DELETE FROM `collaborators` WHERE user_id = OLD.id;
    DELETE FROM `comments` WHERE user_id = OLD.id;
    DELETE FROM `orders` WHERE owner_id = OLD.id;
    DELETE FROM `tasks` WHERE owner_id = OLD.id;
    DELETE FROM `tokens` WHERE user_id = OLD.id;
END;;

DELIMITER ;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `user_roles`;

-- 2023-09-05 12:07:20
