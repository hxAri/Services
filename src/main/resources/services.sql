-- Adminer 4.8.1 MySQL 10.6.12-MariaDB-0ubuntu0.22.04.1 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `services`;
CREATE DATABASE `services` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `services`;

DROP TABLE IF EXISTS `pages`;
CREATE TABLE `pages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `counts` int(11) NOT NULL,
  `created` datetime(6) NOT NULL,
  `expires` datetime(6) NOT NULL,
  `filter` varchar(32) NOT NULL,
  `offsets` int(11) DEFAULT NULL,
  `sizes` bigint(20) NOT NULL,
  `token` varchar(128) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `value` varchar(32) NOT NULL,
  `owner` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdh756hjatikdn7xwf16j93214` (`owner`),
  CONSTRAINT `FKdh756hjatikdn7xwf16j93214` FOREIGN KEY (`owner`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `pages`;
INSERT INTO `pages` (`id`, `counts`, `created`, `expires`, `filter`, `offsets`, `sizes`, `token`, `updated`, `value`, `owner`) VALUES
(4,	10,	'2023-09-11 15:33:44.000000',	'2023-09-11 15:53:44.000000',	'fetchByOwner',	1,	130,	'bd07fb4b5f1944b7f0b5b60deeb4a5219bfbcec07f2ddafbe4f44c34d26ee8d6',	'2023-09-11 15:33:44.000000',	'1',	1),
(5,	10,	'2023-09-11 15:38:47.000000',	'2023-09-11 15:58:47.000000',	'fetchByOwner',	2,	130,	'faca858bb371f7028345fb2f56ed82e5b7994370d79237055825b643474f0b4d',	'2023-09-11 15:38:47.000000',	'1',	1),
(6,	10,	'2023-09-11 15:38:57.000000',	'2023-09-11 15:58:57.000000',	'fetchByOwner',	1,	130,	'bbb2ad44fa03df0b01ea5f7533d12c01de8ee33a965d727befe73a37a92416a8',	'2023-09-11 15:38:57.000000',	'1',	1),
(7,	10,	'2023-09-11 15:41:15.000000',	'2023-09-11 16:01:15.000000',	'fetchByOwner',	1,	130,	'53a95a42b14cce0693704c05c3068f7d1cf832c41dc377102cd8145f52e829ea',	'2023-09-11 15:41:15.000000',	'1',	1),
(8,	4,	'2023-09-11 17:58:50.000000',	'2023-09-11 18:18:50.000000',	'fetchByOwner',	1,	130,	'6937a26cac2c1129e9b7061a75ea518913ae157eb5cc954a27ca18c6090410a9',	'2023-09-11 17:58:50.000000',	'1',	1);

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `roles`;
INSERT INTO `roles` (`id`, `role`) VALUES
(1,	'ROLE_USER'),
(2,	'ROLE_USER');

DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `owner` bigint(20) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK85myl0jt783ssjwxyky9419qe` (`owner`),
  CONSTRAINT `FK85myl0jt783ssjwxyky9419qe` FOREIGN KEY (`owner`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `tasks`;
INSERT INTO `tasks` (`id`, `title`, `owner`, `created`, `updated`) VALUES
(1,	'Belajar Matematika',	1,	'2023-09-01 12:34:56.789000',	'2023-09-01 12:34:56.789000'),
(2,	'Les Fisika',	2,	'2023-08-30 10:15:30.123000',	'2023-08-31 08:45:12.345000'),
(3,	'Ekskul Futsal',	1,	'2023-08-25 14:20:00.987000',	'2023-08-25 14:20:00.987000'),
(4,	'Pelatihan Bahasa Inggris',	2,	'2023-07-15 09:45:30.456000',	'2023-07-20 11:25:55.555000'),
(5,	'Belajar Sejarah',	1,	'2023-06-10 18:00:00.000000',	'2023-06-12 20:30:45.678000'),
(6,	'Les Biologi',	2,	'2023-05-05 22:40:10.234000',	'2023-05-06 15:10:11.111000'),
(7,	'Ekskul Seni Lukis',	1,	'2023-04-01 02:15:20.111000',	'2023-04-02 04:30:33.222000'),
(8,	'Pelatihan Komputer',	2,	'2023-03-15 08:30:45.789000',	'2023-03-18 14:55:00.000000'),
(9,	'Belajar Kimia',	1,	'2023-02-09 17:25:00.123000',	'2023-02-11 19:40:22.333000'),
(10,	'Ekskul Bulu Tangkis',	2,	'2023-01-03 11:11:11.111000',	'2023-01-04 09:22:22.222000'),
(11,	'Les Musik',	1,	'2022-12-01 09:45:30.456000',	'2022-12-05 14:25:45.123000'),
(12,	'Belajar Bahasa Spanyol',	2,	'2022-11-25 16:20:00.987000',	'2022-11-26 08:10:15.234000'),
(13,	'Ekskul Renang',	1,	'2022-10-20 20:30:00.000000',	'2022-10-21 09:30:45.678000'),
(14,	'Pelatihan Kewirausahaan',	2,	'2022-09-15 01:05:10.234000',	'2022-09-16 05:20:30.111000'),
(15,	'Belajar Bahasa Prancis',	1,	'2022-08-10 05:55:20.111000',	'2022-08-12 11:15:22.333000'),
(16,	'Ekskul Basket',	2,	'2022-07-05 12:30:45.789000',	'2022-07-07 15:40:00.000000'),
(17,	'Les Bahasa Jepang',	1,	'2022-06-01 23:15:00.123000',	'2022-06-03 08:10:11.111000'),
(18,	'Pelatihan Desain Grafis',	2,	'2022-05-25 19:10:11.111000',	'2022-05-26 21:45:55.555000'),
(19,	'Ekskul Teater',	1,	'2022-04-20 14:45:00.987000',	'2022-04-21 18:30:33.222000'),
(20,	'Belajar Programming',	2,	'2022-03-15 08:20:45.789000',	'2022-03-16 11:10:00.000000'),
(21,	'Pelatihan Manajemen Waktu',	1,	'2022-02-10 17:55:30.456000',	'2022-02-11 19:25:45.678000'),
(22,	'Ekskul Kesenian',	2,	'2022-01-05 22:00:00.000000',	'2022-01-06 03:15:10.234000'),
(23,	'Les Bahasa Arab',	1,	'2021-12-01 02:25:10.234000',	'2021-12-02 08:30:20.111000'),
(24,	'Belajar Ekonomi',	2,	'2021-11-25 06:15:00.123000',	'2021-11-26 09:45:11.111000'),
(25,	'Ekskul Taekwondo',	1,	'2021-10-20 12:10:30.456000',	'2021-10-21 15:25:45.789000'),
(26,	'Pelatihan Public Speaking',	2,	'2021-09-15 20:30:00.000000',	'2021-09-16 21:55:22.333000'),
(27,	'Belajar Astronomi',	1,	'2021-08-10 03:45:45.678000',	'2021-08-11 05:15:00.000000'),
(28,	'Ekskul Panjat Tebing',	2,	'2021-07-05 09:00:10.234000',	'2021-07-06 12:20:10.111000'),
(29,	'Les Bahasa Korea',	1,	'2021-06-01 15:55:20.111000',	'2021-06-02 18:45:30.789000'),
(30,	'Pelatihan Keuangan Pribadi',	2,	'2021-05-25 22:20:00.987000',	'2021-05-26 23:30:45.456000'),
(31,	'Ekskul Bulu Tangkis',	1,	'2021-04-20 12:10:30.456000',	'2021-04-21 15:25:45.789000'),
(32,	'Belajar Bahasa Spanyol',	2,	'2021-03-15 20:30:00.000000',	'2021-03-16 21:55:22.333000'),
(33,	'Ekskul Renang',	1,	'2021-02-10 03:45:45.678000',	'2021-02-11 05:15:00.000000'),
(34,	'Pelatihan Kewirausahaan',	2,	'2021-01-05 09:00:10.234000',	'2021-01-06 12:20:10.111000'),
(35,	'Belajar Bahasa Jerman',	1,	'2020-12-01 15:55:20.111000',	'2020-12-02 18:45:30.789000'),
(36,	'Ekskul Bulu Tangkis',	2,	'2020-11-25 22:20:00.987000',	'2020-11-26 23:30:45.456000'),
(37,	'Belajar Bahasa Mandarin',	1,	'2020-10-20 12:10:30.456000',	'2020-10-21 15:25:45.789000'),
(38,	'Pelatihan Desain Grafis',	2,	'2020-09-15 20:30:00.000000',	'2020-09-16 21:55:22.333000'),
(39,	'Ekskul Seni Lukis',	1,	'2020-08-10 03:45:45.678000',	'2020-08-11 05:15:00.000000'),
(40,	'Belajar Fisika',	2,	'2020-07-05 09:00:10.234000',	'2020-07-06 12:20:10.111000'),
(41,	'Ekskul Tari',	1,	'2020-06-01 15:55:20.111000',	'2020-06-02 18:45:30.789000'),
(42,	'Pelatihan Keuangan Pribadi',	2,	'2020-05-25 22:20:00.987000',	'2020-05-26 23:30:45.456000'),
(43,	'Belajar Kimia',	1,	'2020-04-20 12:10:30.456000',	'2020-04-21 15:25:45.789000'),
(44,	'Ekskul Teater',	2,	'2020-03-15 20:30:00.000000',	'2020-03-16 21:55:22.333000'),
(45,	'Pelatihan Manajemen Waktu',	1,	'2020-02-10 03:45:45.678000',	'2020-02-11 05:15:00.000000'),
(46,	'Belajar Bahasa Italia',	2,	'2020-01-05 09:00:10.234000',	'2020-01-06 12:20:10.111000'),
(47,	'Ekskul Bulu Tangkis',	1,	'2019-12-01 15:55:20.111000',	'2019-12-02 18:45:30.789000'),
(48,	'Belajar Bahasa Rusia',	2,	'2019-11-25 22:20:00.987000',	'2019-11-26 23:30:45.456000'),
(49,	'Ekskul Panjat Tebing',	1,	'2019-10-20 12:10:30.456000',	'2019-10-21 15:25:45.789000'),
(50,	'Pelatihan Public Speaking',	2,	'2019-09-15 20:30:00.000000',	'2019-09-16 21:55:22.333000'),
(51,	'Belajar Geografi',	1,	'2019-08-10 03:45:45.678000',	'2019-08-11 05:15:00.000000'),
(52,	'Ekskul Sepak Bola',	2,	'2019-07-05 09:00:10.234000',	'2019-07-06 12:20:10.111000'),
(53,	'Pelatihan Bahasa Mandarin',	1,	'2019-06-01 15:55:20.111000',	'2019-06-02 18:45:30.789000'),
(54,	'Belajar Seni Kerajinan',	2,	'2019-05-25 22:20:00.987000',	'2019-05-26 23:30:45.456000'),
(55,	'Ekskul Bela Diri',	1,	'2019-04-20 12:10:30.456000',	'2019-04-21 15:25:45.789000'),
(56,	'Pelatihan Keterampilan Komunikasi',	2,	'2019-03-15 20:30:00.000000',	'2019-03-16 21:55:22.333000'),
(57,	'Belajar Ekonomi',	1,	'2019-02-10 03:45:45.678000',	'2019-02-11 05:15:00.000000'),
(58,	'Ekskul Teater',	2,	'2019-01-05 09:00:10.234000',	'2019-01-06 12:20:10.111000'),
(59,	'Pelatihan Manajemen Waktu',	1,	'2018-12-01 15:55:20.111000',	'2018-12-02 18:45:30.789000'),
(60,	'Belajar Bahasa Italia',	2,	'2018-11-25 22:20:00.987000',	'2018-11-26 23:30:45.456000'),
(61,	'Ekskul Bulu Tangkis',	1,	'2018-10-20 12:10:30.456000',	'2018-10-21 15:25:45.789000'),
(62,	'Belajar Bahasa Rusia',	2,	'2018-09-15 20:30:00.000000',	'2018-09-16 21:55:22.333000'),
(63,	'Ekskul Panjat Tebing',	1,	'2018-08-10 03:45:45.678000',	'2018-08-11 05:15:00.000000'),
(64,	'Pelatihan Public Speaking',	2,	'2018-07-05 09:00:10.234000',	'2018-07-06 12:20:10.111000'),
(65,	'Belajar Kimia',	1,	'2018-06-01 15:55:20.111000',	'2018-06-02 18:45:30.789000'),
(66,	'Ekskul Tari',	2,	'2018-05-25 22:20:00.987000',	'2018-05-26 23:30:45.456000'),
(67,	'Pelatihan Keuangan Pribadi',	1,	'2018-04-20 12:10:30.456000',	'2018-04-21 15:25:45.789000'),
(68,	'Belajar Fisika',	2,	'2018-03-15 20:30:00.000000',	'2018-03-16 21:55:22.333000'),
(69,	'Ekskul Kesenian',	1,	'2018-02-10 03:45:45.678000',	'2018-02-11 05:15:00.000000'),
(70,	'Pelatihan Desain Grafis',	2,	'2018-01-05 09:00:10.234000',	'2018-01-06 12:20:10.111000'),
(71,	'Belajar Bahasa Korea',	1,	'2017-12-01 15:55:20.111000',	'2017-12-02 18:45:30.789000'),
(72,	'Ekskul Bulu Tangkis',	2,	'2017-11-25 22:20:00.987000',	'2017-11-26 23:30:45.456000'),
(73,	'Pelatihan Bahasa Prancis',	1,	'2017-10-20 12:10:30.456000',	'2017-10-21 15:25:45.789000'),
(74,	'Belajar Bahasa Spanyol',	2,	'2017-09-15 20:30:00.000000',	'2017-09-16 21:55:22.333000'),
(75,	'Ekskul Seni Lukis',	1,	'2017-08-10 03:45:45.678000',	'2017-08-11 05:15:00.000000'),
(76,	'Pelatihan Komputer',	2,	'2017-07-05 09:00:10.234000',	'2017-07-06 12:20:10.111000'),
(77,	'Belajar Matematika',	1,	'2017-06-01 15:55:20.111000',	'2017-06-02 18:45:30.789000'),
(78,	'Ekskul Futsal',	2,	'2017-05-25 22:20:00.987000',	'2017-05-26 23:30:45.456000'),
(79,	'Pelatihan Keuangan Pribadi',	1,	'2017-04-20 12:10:30.456000',	'2017-04-21 15:25:45.789000'),
(80,	'Belajar Bahasa Mandarin',	2,	'2017-03-15 20:30:00.000000',	'2017-03-16 21:55:22.333000'),
(81,	'Ekskul Panjat Tebing',	1,	'2017-02-10 03:45:45.678000',	'2017-02-11 05:15:00.000000'),
(82,	'Pelatihan Manajemen Waktu',	2,	'2017-01-05 09:00:10.234000',	'2017-01-06 12:20:10.111000'),
(83,	'Belajar Kimia',	1,	'2016-12-01 15:55:20.111000',	'2016-12-02 18:45:30.789000'),
(84,	'Ekskul Musik',	2,	'2016-11-25 22:20:00.987000',	'2016-11-26 23:30:45.456000'),
(85,	'Pelatihan Kewirausahaan',	1,	'2016-10-20 12:10:30.456000',	'2016-10-21 15:25:45.789000'),
(86,	'Belajar Bahasa Jepang',	2,	'2016-09-15 20:30:00.000000',	'2016-09-16 21:55:22.333000'),
(87,	'Ekskul Tari',	1,	'2016-08-10 03:45:45.678000',	'2016-08-11 05:15:00.000000'),
(88,	'Pelatihan Desain Grafis',	2,	'2016-07-05 09:00:10.234000',	'2016-07-06 12:20:10.111000'),
(89,	'Belajar Ekonomi',	1,	'2016-06-01 15:55:20.111000',	'2016-06-02 18:45:30.789000'),
(90,	'Ekskul Renang',	2,	'2016-05-25 22:20:00.987000',	'2016-05-26 23:30:45.456000'),
(91,	'Pelatihan Public Speaking',	1,	'2016-04-20 12:10:30.456000',	'2016-04-21 15:25:45.789000'),
(92,	'Belajar Fisika',	2,	'2016-03-15 20:30:00.000000',	'2016-03-16 21:55:22.333000'),
(93,	'Ekskul Basket',	1,	'2016-02-10 03:45:45.678000',	'2016-02-11 05:15:00.000000'),
(94,	'Pelatihan Bahasa Inggris',	2,	'2016-01-05 09:00:10.234000',	'2016-01-06 12:20:10.111000'),
(95,	'Belajar Seni Kerajinan',	1,	'2015-12-01 15:55:20.111000',	'2015-12-02 18:45:30.789000'),
(96,	'Ekskul Taekwondo',	2,	'2015-11-25 22:20:00.987000',	'2015-11-26 23:30:45.456000'),
(97,	'Pelatihan Keterampilan Komunikasi',	1,	'2015-10-20 12:10:30.456000',	'2015-10-21 15:25:45.789000'),
(98,	'Belajar Astronomi',	2,	'2015-09-15 20:30:00.000000',	'2015-09-16 21:55:22.333000'),
(99,	'Ekskul Bela Diri',	1,	'2015-08-10 03:45:45.678000',	'2015-08-11 05:15:00.000000'),
(100,	'Pelatihan Keuangan Pribadi',	2,	'2015-07-05 09:00:10.234000',	'2015-07-06 12:20:10.111000'),
(101,	'Belajar Matematika',	1,	'2015-06-01 15:55:20.111000',	'2015-06-02 18:45:30.789000'),
(102,	'Ekskul Futsal',	2,	'2015-05-25 22:20:00.987000',	'2015-05-26 23:30:45.456000'),
(103,	'Pelatihan Bahasa Mandarin',	1,	'2015-04-20 12:10:30.456000',	'2015-04-21 15:25:45.789000'),
(104,	'Belajar Bahasa Inggris',	2,	'2015-03-15 20:30:00.000000',	'2015-03-16 21:55:22.333000'),
(105,	'Ekskul Seni Lukis',	1,	'2015-02-10 03:45:45.678000',	'2015-02-11 05:15:00.000000'),
(106,	'Pelatihan Komputer',	2,	'2015-01-05 09:00:10.234000',	'2015-01-06 12:20:10.111000'),
(107,	'Belajar Kimia',	1,	'2014-12-01 15:55:20.111000',	'2014-12-02 18:45:30.789000'),
(108,	'Ekskul Bulu Tangkis',	2,	'2014-11-25 22:20:00.987000',	'2014-11-26 23:30:45.456000'),
(109,	'Pelatihan Manajemen Waktu',	1,	'2014-10-20 12:10:30.456000',	'2014-10-21 15:25:45.789000'),
(110,	'Belajar Bahasa Italia',	2,	'2014-09-15 20:30:00.000000',	'2014-09-16 21:55:22.333000'),
(111,	'Ekskul Panjat Tebing',	1,	'2014-08-10 03:45:45.678000',	'2014-08-11 05:15:00.000000'),
(112,	'Pelatihan Public Speaking',	2,	'2014-07-05 09:00:10.234000',	'2014-07-06 12:20:10.111000'),
(113,	'Belajar Fisika',	1,	'2014-06-01 15:55:20.111000',	'2014-06-02 18:45:30.789000'),
(114,	'Ekskul Tari',	2,	'2014-05-25 22:20:00.987000',	'2014-05-26 23:30:45.456000'),
(115,	'Pelatihan Keuangan Pribadi',	1,	'2014-04-20 12:10:30.456000',	'2014-04-21 15:25:45.789000'),
(116,	'Belajar Bahasa Jepang',	2,	'2014-03-15 20:30:00.000000',	'2014-03-16 21:55:22.333000'),
(117,	'Ekskul Musik',	1,	'2014-02-10 03:45:45.678000',	'2014-02-11 05:15:00.000000'),
(118,	'Pelatihan Kewirausahaan',	2,	'2014-01-05 09:00:10.234000',	'2014-01-06 12:20:10.111000'),
(119,	'Belajar Bahasa Spanyol',	1,	'2013-12-01 15:55:20.111000',	'2013-12-02 18:45:30.789000'),
(120,	'Ekskul Bela Diri',	2,	'2013-11-25 22:20:00.987000',	'2013-11-26 23:30:45.456000'),
(121,	'Pelatihan Keterampilan Komunikasi',	1,	'2013-10-20 12:10:30.456000',	'2013-10-21 15:25:45.789000'),
(122,	'Belajar Astronomi',	2,	'2013-09-15 20:30:00.000000',	'2013-09-16 21:55:22.333000'),
(123,	'Ekskul Sepak Bola',	1,	'2013-08-10 03:45:45.678000',	'2013-08-11 05:15:00.000000'),
(124,	'Pelatihan Bahasa Mandarin',	2,	'2013-07-05 09:00:10.234000',	'2013-07-06 12:20:10.111000'),
(125,	'Belajar Bahasa Inggris',	1,	'2013-06-01 15:55:20.111000',	'2013-06-02 18:45:30.789000'),
(126,	'Ekskul Seni Lukis',	2,	'2013-05-25 22:20:00.987000',	'2013-05-26 23:30:45.456000'),
(127,	'Pelatihan Komputer',	1,	'2013-04-20 12:10:30.456000',	'2013-04-21 15:25:45.789000'),
(128,	'Belajar Kimia',	2,	'2013-03-15 20:30:00.000000',	'2013-03-16 21:55:22.333000'),
(129,	'Ekskul Bulu Tangkis',	1,	'2013-02-10 03:45:45.678000',	'2013-02-11 05:15:00.000000'),
(130,	'Pelatihan Manajemen Waktu',	2,	'2013-01-05 09:00:10.234000',	'2013-01-06 12:20:10.111000');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` enum('ADMIN','USER') NOT NULL,
  `fullname` varchar(32) NOT NULL,
  `usermail` varchar(48) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(258) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1nv2bs798bp1ieo14fv12t4on` (`usermail`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `users`;
INSERT INTO `users` (`id`, `role`, `fullname`, `usermail`, `username`, `password`, `created`, `updated`) VALUES
(1,	'ADMIN',	'Test',	'example@example.com',	'example',	'$2a$10$Tnu4tW1C1qLRl/zCVZApduQsb7OTC//D896MGcP4gRAviYSlWG.UG',	'2023-09-07 17:27:36.000000',	'2023-09-08 16:22:50.000000'),
(2,	'ADMIN',	'Test',	'test@test.io',	'test',	'$2a$10$OvO2fdwqNQg/BjkZBhinge/nBDXxdmWT3JaFI.VCwXOvylUVAdFua',	'2023-09-08 23:26:40.000000',	'2023-09-08 23:26:40.000000');

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE `users_roles`;
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES
(1,	1),
(2,	2);

-- 2023-09-19 11:48:30
