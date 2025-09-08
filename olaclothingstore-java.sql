-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.41 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ola_clothing_store
CREATE DATABASE IF NOT EXISTS `ola_clothing_store` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ola_clothing_store`;

-- Dumping structure for table ola_clothing_store.address
CREATE TABLE IF NOT EXISTS `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `address_line_1` varchar(255) DEFAULT NULL,
  `address_line_2` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `postal_code` varchar(20) DEFAULT NULL,
  `city_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `fk_address_city1_idx` (`city_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.address: ~9 rows (approximately)
REPLACE INTO `address` (`id`, `user_id`, `address_line_1`, `address_line_2`, `postal_code`, `city_id`) VALUES
	(1, 1, '12/A', 'Main Street, Colombo', '12312', 1),
	(2, 1, '45/B', 'Station Road, Galle', '134321', 2),
	(3, 1, '78/C', 'Market Lane, Kandy', NULL, 7),
	(6, 2, '101/D', 'Temple Road, Matara', '43535', 14),
	(7, 2, '22/E', 'Lake View, Kurunegala', '43535', 13),
	(8, 1, '56/F', 'Sea Avenue, Negombo', '11370', 14),
	(9, 2, '33/G', 'Hill Street, Nuwara Eliya', '11370', 13),
	(10, 1, '88/H', 'Park Road, Colombo', '11370', 1),
	(12, 26, '19/J', 'Garden Road, Negombo', '11370', 1),
	(13, 29, '77/K', 'Station Road, Kurunegala', '11370', 1),
	(14, 30, '25/L', 'River Side, Gampaha', '11370', 1);

-- Dumping structure for table ola_clothing_store.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.brand: ~5 rows (approximately)
REPLACE INTO `brand` (`id`, `name`) VALUES
	(1, 'Nike'),
	(2, 'Adidas'),
	(3, 'Puma'),
	(4, 'Zara'),
	(5, 'H&M');

-- Dumping structure for table ola_clothing_store.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.cart: ~0 rows (approximately)

-- Dumping structure for table ola_clothing_store.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.category: ~6 rows (approximately)
REPLACE INTO `category` (`id`, `name`) VALUES
	(1, 'Men'),
	(2, 'Women'),
	(3, 'Kids'),
	(4, 'Accessories'),
	(5, 'Home Items');

-- Dumping structure for table ola_clothing_store.city
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `province_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_city_province1_idx` (`province_id`),
  CONSTRAINT `fk_city_province1` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.city: ~15 rows (approximately)
REPLACE INTO `city` (`id`, `name`, `province_id`) VALUES
	(1, 'Colombo', 9),
	(2, 'Dehiwala-Mount Lavinia', 9),
	(3, 'Moratuwa', 9),
	(4, 'Kandy', 1),
	(5, 'Matale', 1),
	(6, 'Nuwara Eliya', 1),
	(7, 'Galle', 7),
	(8, 'Matara', 7),
	(9, 'Hambantota', 7),
	(10, 'Jaffna', 3),
	(11, 'Kilinochchi', 3),
	(12, 'Vavuniya', 3),
	(13, 'Batticaloa', 2),
	(14, 'Trincomalee', 2),
	(15, 'Kalmunai', 2);

-- Dumping structure for table ola_clothing_store.color
CREATE TABLE IF NOT EXISTS `color` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.color: ~5 rows (approximately)
REPLACE INTO `color` (`id`, `name`) VALUES
	(1, 'Red'),
	(2, 'Blue'),
	(3, 'Green'),
	(4, 'Black'),
	(5, 'White');

-- Dumping structure for table ola_clothing_store.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT (now()),
  `address_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `fk_orders_address1_idx` (`address_id`),
  CONSTRAINT `fk_orders_address1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.orders: ~28 rows (approximately)
REPLACE INTO `orders` (`id`, `user_id`, `created_at`, `address_id`) VALUES
	(1, 1, '2025-08-08 12:25:36', 8),
	(2, 1, '2025-08-08 12:28:54', 8),
	(3, 2, '2025-08-08 12:30:13', 9),
	(4, 2, '2025-08-08 12:35:36', 9),
	(5, 1, '2025-08-08 12:37:38', 8),
	(6, 2, '2025-08-08 12:41:02', 9),
	(7, 2, '2025-08-08 12:43:37', 9),
	(8, 2, '2025-08-08 12:44:51', 7),
	(9, 2, '2025-08-08 13:04:53', 9),
	(10, 2, '2025-08-08 13:10:42', 9),
	(11, 2, '2025-08-08 13:12:42', 7),
	(12, 2, '2025-08-08 13:14:22', 7),
	(14, 2, '2025-08-08 13:14:54', 7),
	(15, 1, '2025-08-08 21:42:54', 10),
	(16, 2, '2025-08-09 19:09:46', 9),
	(17, 2, '2025-08-09 19:10:45', 7),
	(18, 2, '2025-08-09 19:13:19', 9),
	(19, 2, '2025-08-09 19:20:47', 6),
	(20, 2, '2025-08-09 19:26:56', 9),
	(22, 2, '2025-08-09 19:31:53', 9),
	(23, 2, '2025-08-09 19:32:10', 9),
	(24, 2, '2025-08-09 19:36:38', 7),
	(25, 26, '2025-08-09 21:20:55', 12),
	(26, 2, '2025-08-13 18:33:59', 9),
	(27, 2, '2025-08-14 10:13:01', 7),
	(28, 2, '2025-08-14 10:14:07', 7),
	(29, 29, '2025-08-14 14:51:15', 13),
	(30, 30, '2025-08-14 15:10:31', 14),
	(31, 2, '2025-08-14 18:58:27', 9);

-- Dumping structure for table ola_clothing_store.order_item
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.order_item: ~30 rows (approximately)
REPLACE INTO `order_item` (`id`, `order_id`, `product_id`, `qty`) VALUES
	(1, 1, 3, 1),
	(2, 1, 4, 1),
	(3, 1, 6, 2),
	(4, 3, 2, 1),
	(5, 3, 4, 2),
	(6, 4, 6, 2),
	(7, 4, 4, 1),
	(8, 5, 4, 1),
	(9, 6, 3, 1),
	(10, 6, 5, 1),
	(11, 7, 1, 1),
	(12, 8, 1, 1),
	(13, 9, 3, 1),
	(14, 10, 3, 1),
	(15, 11, 4, 1),
	(16, 12, 3, 1),
	(18, 15, 3, 2),
	(19, 16, 3, 2),
	(20, 17, 4, 5),
	(21, 18, 3, 8),
	(22, 19, 1, 1),
	(23, 20, 18, 5),
	(25, 23, 4, 1),
	(26, 24, 3, 1),
	(27, 25, 18, 1),
	(28, 25, 1, 1),
	(29, 26, 9, 1),
	(30, 26, 5, 1),
	(31, 26, 3, 1),
	(32, 26, 6, 1),
	(33, 26, 19, 1),
	(34, 27, 3, 1),
	(35, 28, 4, 1),
	(36, 29, 9, 1),
	(37, 30, 5, 1),
	(38, 31, 3, 1);

-- Dumping structure for table ola_clothing_store.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text,
  `price` double DEFAULT NULL,
  `qty` int NOT NULL,
  `category_id` int DEFAULT NULL,
  `sub_category_id` int DEFAULT NULL,
  `color_id` int DEFAULT NULL,
  `size_id` int DEFAULT NULL,
  `brand_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `product_status_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `sub_category_id` (`sub_category_id`),
  KEY `color_id` (`color_id`),
  KEY `size_id` (`size_id`),
  KEY `brand_id` (`brand_id`),
  KEY `fk_product_product_status1_idx` (`product_status_id`),
  CONSTRAINT `fk_product_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`sub_category_id`) REFERENCES `sub_category` (`id`),
  CONSTRAINT `product_ibfk_3` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`),
  CONSTRAINT `product_ibfk_4` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`),
  CONSTRAINT `product_ibfk_6` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.product: ~18 rows (approximately)
REPLACE INTO `product` (`id`, `title`, `description`, `price`, `qty`, `category_id`, `sub_category_id`, `color_id`, `size_id`, `brand_id`, `created_at`, `product_status_id`) VALUES
	(1, 'Carnage Classic Seamless', 'Comfortable running shoes for men', 99, 46, 1, 1, 1, 3, 1, '2025-07-23 18:30:00', 1),
	(2, 'Classic White Cotton T-Shirt', 'Soft, breathable, and perfect for everyday wear.', 89, 99, 1, 2, 2, 2, 2, '2025-07-23 18:30:00', 2),
	(3, 'Oversized Linen Shirt', 'Lightweight and relaxed, ideal for warm days.', 70, 39, 1, 3, 3, 4, 3, '2025-07-23 18:30:00', 1),
	(4, 'Zara Summer Dress', 'Lightweight dress for women', 49, 17, 1, 3, 4, 2, 4, '2025-07-23 18:30:00', 1),
	(5, 'H&M Kids T-Shirt', 'Fun and colorful T-shirt for kids', 59, 117, 1, 5, 5, 1, 5, '2025-07-23 18:30:00', 1),
	(6, 'Black Leather Bag', 'Elegant black leather bag for women', 69, 20, 1, 6, 1, 2, 4, '2025-07-23 18:30:00', 1),
	(8, 'Slim Fit Polo Tee', 'Sharp and casual, perfect for smart-casual looks.\n\n', 79, 12, 1, 2, 1, 3, 3, '2025-07-24 18:30:00', 1),
	(9, 'Graphic Streetwear Hoodie', 'Bold print with a cozy fleece lining', 99, 1, 2, 3, 3, 3, 2, '2025-07-24 18:30:00', 1),
	(10, 'Cropped Ribbed Tank Top', 'Stretchy fit with a flattering cut', 29, 234, 1, 1, 2, 1, 2, '2025-07-24 18:30:00', 1),
	(11, 'Classic Denim Jacket', 'Vintage wash with durable stitching.', 79, 434, 1, 1, 2, 3, 1, '2025-07-24 18:30:00', 1),
	(12, 'Tailored Blazer Jacket', 'Sharp structure for a polished look.', 69, 23, 2, 3, 1, 4, 1, '2025-07-25 18:30:00', 1),
	(13, 'Puffer Vest', 'Lightweight warmth with a modern silhouette.', 79, 20, 2, 3, 2, 2, 1, '2025-07-25 18:30:00', 1),
	(14, 'Longline Trench Coat', 'Timeless style with a water-repellent finish.', 89, 7, 2, 3, 1, 2, 1, '2025-07-25 18:30:00', 2),
	(15, 'Oversized Knit Cardigan', 'Cozy texture with deep pockets.', 199, 20, 4, 6, 4, 3, 1, '2025-07-26 18:30:00', 1),
	(16, 'Silk Satin Blouse', 'Elegant and smooth, perfect for office or evening wear.', 199, 10, 2, 3, 2, 2, 4, '2025-08-08 18:30:00', 1),
	(17, 'Oversized Knit Sweater', 'Soft, cozy, and ideal for layering in cool weather.', 299, 12, 2, 3, 3, 1, 2, '2025-08-08 18:30:00', 1),
	(18, 'Floral Wrap Midi Dress', 'Adjustable waist with a feminine, flowy silhouette.', 2000, 116, 2, 3, 3, 4, 4, '2025-08-09 18:30:00', 1),
	(19, 'Off-Shoulder Summer Dress', 'Breezy and romantic for warm, sunny days.', 200, 32, 2, 3, 5, 3, 1, '2025-08-09 18:30:00', 1),
	(20, 'Desire Oversize Tee', 'Stylish, comfortable womenâs clothing for every occasion', 100, 10, 1, 2, 3, 2, 1, '2025-08-13 18:30:00', 1),
	(21, 'Desire Oversize Tee', 'Stylish, comfortable womenâs clothing for every occasion', 199, 20, 1, 1, 2, 2, 2, '2025-08-13 18:30:00', 1);

-- Dumping structure for table ola_clothing_store.product_image
CREATE TABLE IF NOT EXISTS `product_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.product_image: ~0 rows (approximately)

-- Dumping structure for table ola_clothing_store.product_status
CREATE TABLE IF NOT EXISTS `product_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.product_status: ~3 rows (approximately)
REPLACE INTO `product_status` (`id`, `name`) VALUES
	(1, 'Available'),
	(2, 'Out Of Stock'),
	(3, 'Discontinued');

-- Dumping structure for table ola_clothing_store.province
CREATE TABLE IF NOT EXISTS `province` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.province: ~9 rows (approximately)
REPLACE INTO `province` (`id`, `name`) VALUES
	(1, 'Central Province'),
	(2, 'Eastern Province'),
	(3, 'Northern Province'),
	(4, 'North Central Province'),
	(5, 'North Western Province'),
	(6, 'Sabaragamuwa Province'),
	(7, 'Southern Province'),
	(8, 'Uva Province'),
	(9, 'Western Province');

-- Dumping structure for table ola_clothing_store.size
CREATE TABLE IF NOT EXISTS `size` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.size: ~5 rows (approximately)
REPLACE INTO `size` (`id`, `name`) VALUES
	(1, 'S'),
	(2, 'M'),
	(3, 'L'),
	(4, 'XL'),
	(5, 'XXL');

-- Dumping structure for table ola_clothing_store.sub_category
CREATE TABLE IF NOT EXISTS `sub_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `sub_category_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.sub_category: ~6 rows (approximately)
REPLACE INTO `sub_category` (`id`, `category_id`, `name`) VALUES
	(1, 1, 'Shirts'),
	(2, 1, 'T-Shirts'),
	(3, 2, 'Dresses'),
	(4, 2, 'Skirts'),
	(5, 3, 'T-Shirts'),
	(6, 4, 'Bags');

-- Dumping structure for table ola_clothing_store.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `f_name` varchar(100) NOT NULL,
  `l_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `user_status_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_type_id` int NOT NULL,
  `verification` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_user_user_status1_idx` (`user_status_id`),
  KEY `fk_user_user_type1_idx` (`user_type_id`),
  CONSTRAINT `fk_user_user_status1` FOREIGN KEY (`user_status_id`) REFERENCES `user_status` (`id`),
  CONSTRAINT `fk_user_user_type1` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- Dumping data for table ola_clothing_store.user: ~9 rows (approximately)
REPLACE INTO `user` (`id`, `f_name`, `l_name`, `email`, `password`, `phone`, `user_status_id`, `created_at`, `user_type_id`, `verification`) VALUES
	(1, 'Alex', 'Johnson', 'alex.johnson@example.com', 'Pass@1234', '0711234567', 1, '2025-07-23 11:31:56', 1, 'Verified'),
	(2, 'Maria', 'Lopez', 'maria.lopez@example.com', 'Admin@2025', '0729876543', 1, '2025-07-23 20:56:39', 2, 'Verified'),
	(15, 'John', 'Smith', 'john.smith@example.com', 'User@123', '0754567890', 2, '2025-07-25 21:26:04', 2, 'Verified'),
	(24, 'Emma', 'Brown', 'emma.brown@example.com', 'Temp@111', '0761122334', 1, '2025-08-08 20:10:57', 1, '162126'),
	(25, 'Liam', 'Taylor', 'liam.taylor@example.com', 'Secure@789', '0775566778', 1, '2025-08-08 20:12:33', 1, 'Verified'),
	(26, 'Sophia', 'Davis', 'sophia.davis@example.com', 'Sophia@22', '0789988776', 1, '2025-08-09 21:15:09', 1, 'Verified'),
	(27, 'Ethan', 'Miller', 'ethan.miller@example.com', 'Test@555', '0793344556', 1, '2025-08-14 14:42:33', 1, '564235'),
	(28, 'Olivia', 'Wilson', 'olivia.wilson@example.com', 'Olivia@321', '0702233445', 1, '2025-08-14 14:45:48', 1, 'Verified'),
	(29, 'James', 'Anderson', 'james.anderson@example.com', 'James@4321', '0714455667', 1, '2025-08-14 14:49:53', 1, 'Verified'),
	(30, 'Ava', 'Martinez', 'ava.martinez@example.com', 'Ava@1010', '0723344556', 1, '2025-08-14 15:08:18', 1, 'Verified');


-- Dumping structure for table ola_clothing_store.user_status
CREATE TABLE IF NOT EXISTS `user_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.user_status: ~2 rows (approximately)
REPLACE INTO `user_status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive');

-- Dumping structure for table ola_clothing_store.user_type
CREATE TABLE IF NOT EXISTS `user_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.user_type: ~2 rows (approximately)
REPLACE INTO `user_type` (`id`, `name`) VALUES
	(1, 'User'),
	(2, 'Admin');

-- Dumping structure for table ola_clothing_store.wishlist
CREATE TABLE IF NOT EXISTS `wishlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ola_clothing_store.wishlist: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
