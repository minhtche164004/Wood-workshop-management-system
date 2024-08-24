-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: leadderdeptraivl.mysql.database.azure.com    Database: test1
-- ------------------------------------------------------
-- Server version	8.0.37-azure

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `action_type`
--

DROP TABLE IF EXISTS `action_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `action_type` (
  `action_type_id` int NOT NULL AUTO_INCREMENT,
  `action_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`action_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `advancesalary`
--

DROP TABLE IF EXISTS `advancesalary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `advancesalary` (
  `advance_salary_id` int NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `is_advance_success` bit(1) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `is_approve` bit(1) DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  PRIMARY KEY (`advance_salary_id`),
  KEY `user_id` (`user_id`),
  KEY `FKh7ti9am409kr7he0i7ovkgg9s` (`job_id`),
  CONSTRAINT `advancesalary_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKh7ti9am409kr7he0i7ovkgg9s` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=377 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee_materials`
--

DROP TABLE IF EXISTS `employee_materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_materials` (
  `emp_material_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `product_sub_material_id` int DEFAULT NULL,
  `request_products_submaterials_id` int DEFAULT NULL,
  `total_material` double DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  PRIMARY KEY (`emp_material_id`),
  KEY `FKlu70qu3xy53sr908el4ewt933` (`employee_id`),
  KEY `FK8ust13b9nhy5dkbfi0nss97dc` (`product_sub_material_id`),
  KEY `FK93s1hhmqpwwk8sxbxrsmv9om2` (`request_products_submaterials_id`),
  KEY `FK6w32wv1jekvi16tlgvlqnbpjk` (`job_id`),
  CONSTRAINT `FK6w32wv1jekvi16tlgvlqnbpjk` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`),
  CONSTRAINT `FK8ust13b9nhy5dkbfi0nss97dc` FOREIGN KEY (`product_sub_material_id`) REFERENCES `product_sub_materials` (`product_sub_material_id`),
  CONSTRAINT `FK93s1hhmqpwwk8sxbxrsmv9om2` FOREIGN KEY (`request_products_submaterials_id`) REFERENCES `request_products_submaterials` (`request_products_submaterials_id`),
  CONSTRAINT `FKlu70qu3xy53sr908el4ewt933` FOREIGN KEY (`employee_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=380 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forgot_password`
--

DROP TABLE IF EXISTS `forgot_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forgot_password` (
  `fpid` int NOT NULL AUTO_INCREMENT,
  `expiration_time` datetime(6) NOT NULL,
  `otp` int NOT NULL,
  `user_user_id` int DEFAULT NULL,
  PRIMARY KEY (`fpid`),
  UNIQUE KEY `UK_436rcwp67sud355lgi3s4p1cv` (`user_user_id`),
  CONSTRAINT `FK4smi7oqy3gk1eji1gtnytl9gq` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `information_user`
--

DROP TABLE IF EXISTS `information_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `information_user` (
  `infor_id` int NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(50) DEFAULT NULL,
  `fullname` varchar(50) DEFAULT NULL,
  `address` varchar(250) DEFAULT NULL,
  `bank` varchar(20) DEFAULT NULL,
  `bank_account_number` varchar(20) DEFAULT NULL,
  `city_province` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `district` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `wards` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `has_account` int DEFAULT NULL,
  PRIMARY KEY (`infor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `input_sub_material`
--

DROP TABLE IF EXISTS `input_sub_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `input_sub_material` (
  `input_id` int NOT NULL AUTO_INCREMENT,
  `date_input` datetime(6) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `unit_price` decimal(38,2) DEFAULT NULL,
  `sub_material_id` int DEFAULT NULL,
  `action_type_id` int DEFAULT NULL,
  `out_price` decimal(38,2) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `code_input` varchar(255) DEFAULT NULL,
  `reason_export` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`input_id`),
  KEY `FK32ey1l1wrps7qglujftsexinv` (`sub_material_id`),
  KEY `FK7bani79mn3epgffdbsflgltmf` (`action_type_id`),
  CONSTRAINT `FK32ey1l1wrps7qglujftsexinv` FOREIGN KEY (`sub_material_id`) REFERENCES `sub_materials` (`sub_material_id`),
  CONSTRAINT `FK7bani79mn3epgffdbsflgltmf` FOREIGN KEY (`action_type_id`) REFERENCES `action_type` (`action_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=535 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jobs` (
  `job_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `time_finish` datetime(6) DEFAULT NULL,
  `quantity_product` int DEFAULT NULL,
  `cost` decimal(38,2) DEFAULT NULL,
  `time_start` datetime(6) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `request_product_id` int DEFAULT NULL,
  `order_detail_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `job_log` bit(1) DEFAULT NULL,
  `reassigned` bit(1) DEFAULT NULL,
  `original_quantity_product` int DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  KEY `FK5v6x7t8cdj0vcekwktl1eft59` (`request_product_id`),
  KEY `FK9mawqgqvqfbgsxpmbtvc4kux7` (`order_detail_id`),
  KEY `FKcomuel2dc6xc98j5lxkihastn` (`status_id`),
  CONSTRAINT `FK5rp8qlixeemvvdu8fk3a4ih2m` FOREIGN KEY (`order_detail_id`) REFERENCES `orderdetails` (`order_detail_id`),
  CONSTRAINT `FK5v6x7t8cdj0vcekwktl1eft59` FOREIGN KEY (`request_product_id`) REFERENCES `request_products` (`request_product_id`),
  CONSTRAINT `FK60v5o2j0qplf8h1wtm18mb31x` FOREIGN KEY (`status_id`) REFERENCES `status_job` (`status_id`),
  CONSTRAINT `jobs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `jobs_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1412 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `materials`
--

DROP TABLE IF EXISTS `materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materials` (
  `material_id` int NOT NULL AUTO_INCREMENT,
  `material_name` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`material_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orderdetails`
--

DROP TABLE IF EXISTS `orderdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderdetails` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` decimal(38,2) DEFAULT NULL,
  `request_product_id` int DEFAULT NULL,
  PRIMARY KEY (`order_detail_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  KEY `request_product_id` (`request_product_id`),
  CONSTRAINT `orderdetails_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `orderdetails_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `orderdetails_ibfk_3` FOREIGN KEY (`request_product_id`) REFERENCES `request_products` (`request_product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=372 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_date` datetime DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `special_order` bit(1) DEFAULT NULL,
  `payment_method` int DEFAULT NULL,
  `deposite` decimal(38,2) DEFAULT NULL,
  `infor_id` int DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `city_province` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `wards` varchar(255) DEFAULT NULL,
  `order_finish` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `response` varchar(255) DEFAULT NULL,
  `discount` decimal(38,2) DEFAULT NULL,
  `refund` decimal(38,2) DEFAULT NULL,
  `contract_date` datetime(6) DEFAULT NULL,
  `refund_id` int DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `infor_id` (`infor_id`),
  KEY `FKjiqtdoncm83ebe7alrdhcqda7` (`status_id`),
  KEY `FKo8cpokv9obipamoc2debmp7hq` (`refund_id`),
  CONSTRAINT `FKjiqtdoncm83ebe7alrdhcqda7` FOREIGN KEY (`status_id`) REFERENCES `status_order` (`status_id`),
  CONSTRAINT `FKo8cpokv9obipamoc2debmp7hq` FOREIGN KEY (`refund_id`) REFERENCES `refund_order_status` (`refund_id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`infor_id`) REFERENCES `information_user` (`infor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=305 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `positions`
--

DROP TABLE IF EXISTS `positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `positions` (
  `position_id` int NOT NULL AUTO_INCREMENT,
  `position_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `process_product_error`
--

DROP TABLE IF EXISTS `process_product_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_product_error` (
  `process_product_error_id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_fixed` bit(1) DEFAULT NULL,
  `solution` varchar(255) DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `request_product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`process_product_error_id`),
  KEY `FK2tfk8j3ss17pdp6c1kgvdra34` (`job_id`),
  KEY `FKg5p3w5f0vngx7f7e8ps3s8cq5` (`product_id`),
  KEY `FKmoh505ou60fy2xnndikay0wv0` (`request_product_id`),
  CONSTRAINT `FK2tfk8j3ss17pdp6c1kgvdra34` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`),
  CONSTRAINT `FKg5p3w5f0vngx7f7e8ps3s8cq5` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `FKmoh505ou60fy2xnndikay0wv0` FOREIGN KEY (`request_product_id`) REFERENCES `request_products` (`request_product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_request_images`
--

DROP TABLE IF EXISTS `product_request_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_request_images` (
  `product_image_id` int NOT NULL AUTO_INCREMENT,
  `extension_name` varchar(255) DEFAULT NULL,
  `file_original_name` varchar(255) DEFAULT NULL,
  `full_path` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `request_product_id` int DEFAULT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `FK6ey4o5p8p7us41csl6qhjjlfx` (`request_product_id`),
  CONSTRAINT `FK6ey4o5p8p7us41csl6qhjjlfx` FOREIGN KEY (`request_product_id`) REFERENCES `request_products` (`request_product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_sub_materials`
--

DROP TABLE IF EXISTS `product_sub_materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_sub_materials` (
  `product_sub_material_id` int NOT NULL AUTO_INCREMENT,
  `sub_material_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `input_id` int DEFAULT NULL,
  `sub_material` int NOT NULL,
  PRIMARY KEY (`product_sub_material_id`),
  KEY `sub_material_id` (`sub_material_id`),
  KEY `product_id` (`product_id`),
  KEY `FKsontpag31fkmh10fclno79jj8` (`input_id`),
  CONSTRAINT `FKsontpag31fkmh10fclno79jj8` FOREIGN KEY (`input_id`) REFERENCES `input_sub_material` (`input_id`),
  CONSTRAINT `product_sub_materials_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=324 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `productimages`
--

DROP TABLE IF EXISTS `productimages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productimages` (
  `product_image_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `extension_name` varchar(255) DEFAULT NULL,
  `file_original_name` varchar(255) DEFAULT NULL,
  `full_path` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `productimages_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=307 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `completion_time` date DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `enddate_warranty` date DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `category_id` (`category_id`),
  KEY `FKsngqpr54og4mcg69ijn2sasfg` (`status_id`),
  CONSTRAINT `FKsngqpr54og4mcg69ijn2sasfg` FOREIGN KEY (`status_id`) REFERENCES `status_product` (`status_id`),
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refund_order_status`
--

DROP TABLE IF EXISTS `refund_order_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund_order_status` (
  `refund_id` int NOT NULL AUTO_INCREMENT,
  `refund_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`refund_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request_images`
--

DROP TABLE IF EXISTS `request_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request_images` (
  `product_image_id` int NOT NULL AUTO_INCREMENT,
  `extension_name` varchar(255) DEFAULT NULL,
  `file_original_name` varchar(255) DEFAULT NULL,
  `full_path` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `request_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `FK66yetebhdmco2bmqsndfrec85` (`request_id`),
  KEY `FK7a55ichtysco1sycrm3vysrwr` (`order_id`),
  CONSTRAINT `FK7a55ichtysco1sycrm3vysrwr` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request_products`
--

DROP TABLE IF EXISTS `request_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request_products` (
  `request_product_id` int NOT NULL AUTO_INCREMENT,
  `request_product_name` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `price` decimal(19,4) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `completion_time` date DEFAULT NULL,
  `request_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  PRIMARY KEY (`request_product_id`),
  KEY `FK8m3pan2s459jqlrv1v6nk11rm` (`request_id`),
  KEY `FK7ilmeju320yqe5hnal7whb6gw` (`status_id`),
  KEY `FKme4r4d17um7qon380xxp0qiwv` (`order_id`),
  CONSTRAINT `FK7ilmeju320yqe5hnal7whb6gw` FOREIGN KEY (`status_id`) REFERENCES `status_product` (`status_id`),
  CONSTRAINT `FKme4r4d17um7qon380xxp0qiwv` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request_products_submaterials`
--

DROP TABLE IF EXISTS `request_products_submaterials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request_products_submaterials` (
  `request_products_submaterials_id` int NOT NULL AUTO_INCREMENT,
  `request_product_id` int DEFAULT NULL,
  `sub_material_id` int DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `input_id` int DEFAULT NULL,
  `sub_material` int NOT NULL,
  PRIMARY KEY (`request_products_submaterials_id`),
  UNIQUE KEY `request_product_id` (`request_product_id`,`sub_material_id`),
  KEY `sub_material_id` (`sub_material_id`),
  KEY `FK50ttmnhcigvcx23yc39wweher` (`input_id`),
  CONSTRAINT `FK50ttmnhcigvcx23yc39wweher` FOREIGN KEY (`input_id`) REFERENCES `input_sub_material` (`input_id`),
  CONSTRAINT `request_products_submaterials_ibfk_1` FOREIGN KEY (`request_product_id`) REFERENCES `request_products` (`request_product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=890 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requests`
--

DROP TABLE IF EXISTS `requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests` (
  `request_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `request_date` datetime DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `response` varchar(255) DEFAULT NULL,
  `description` text,
  `code` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `city_province` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `wards` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `user_id` (`user_id`),
  KEY `FKfe2n50twyoyhbygknc3k4ff6b` (`status_id`),
  CONSTRAINT `FKfe2n50twyoyhbygknc3k4ff6b` FOREIGN KEY (`status_id`) REFERENCES `status_request` (`status_id`),
  CONSTRAINT `requests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `salaries`
--

DROP TABLE IF EXISTS `salaries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salaries` (
  `salary_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`salary_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `salaries_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status_job`
--

DROP TABLE IF EXISTS `status_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_job` (
  `status_id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(255) NOT NULL,
  `type` int NOT NULL,
  `des` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status_order`
--

DROP TABLE IF EXISTS `status_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_order` (
  `status_id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(255) NOT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status_product`
--

DROP TABLE IF EXISTS `status_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_product` (
  `status_id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(255) NOT NULL,
  `type` int NOT NULL,
  `descriptions` varchar(255) NOT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status_request`
--

DROP TABLE IF EXISTS `status_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_request` (
  `status_id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(255) NOT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status_user`
--

DROP TABLE IF EXISTS `status_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_user` (
  `status_id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_materials`
--

DROP TABLE IF EXISTS `sub_materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sub_materials` (
  `sub_material_id` int NOT NULL AUTO_INCREMENT,
  `sub_material_name` varchar(255) NOT NULL,
  `material_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sub_material_id`),
  KEY `material_id` (`material_id`),
  CONSTRAINT `sub_materials_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `suppliermaterial`
--

DROP TABLE IF EXISTS `suppliermaterial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliermaterial` (
  `supplier_material` int NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(50) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `sub_material_id` int DEFAULT NULL,
  PRIMARY KEY (`supplier_material`),
  KEY `sub_material_id` (`sub_material_id`),
  CONSTRAINT `suppliermaterial_ibfk_1` FOREIGN KEY (`sub_material_id`) REFERENCES `sub_materials` (`sub_material_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `infor_id` int DEFAULT NULL,
  `position_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `role_id` (`role_id`),
  KEY `infor_id` (`infor_id`),
  KEY `fk_users_positions` (`position_id`),
  KEY `FK6hywckynpmknvf8hnnqldr4mv` (`status_id`),
  CONSTRAINT `FK6hywckynpmknvf8hnnqldr4mv` FOREIGN KEY (`status_id`) REFERENCES `status_user` (`status_id`),
  CONSTRAINT `FK6ph6xiiydudp6umjf2xckbbmi` FOREIGN KEY (`position_id`) REFERENCES `positions` (`position_id`),
  CONSTRAINT `fk_users_positions` FOREIGN KEY (`position_id`) REFERENCES `positions` (`position_id`),
  CONSTRAINT `fk_users_status_user` FOREIGN KEY (`status_id`) REFERENCES `status_user` (`status_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `users_ibfk_3` FOREIGN KEY (`infor_id`) REFERENCES `information_user` (`infor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `whitelist`
--

DROP TABLE IF EXISTS `whitelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `whitelist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3ah7d2rv0856t3qmmkclx5vq5` (`product_id`),
  KEY `FKdra6fv43vgtg0ysv68va5od5e` (`user_id`),
  CONSTRAINT `FK3ah7d2rv0856t3qmmkclx5vq5` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `FKdra6fv43vgtg0ysv68va5od5e` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-23  3:37:47
