-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookingtable`
--

DROP TABLE IF EXISTS `bookingtable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bookingtable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `check_in` date NOT NULL,
  `check_out` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `guest_id_idx` (`guest_id`),
  KEY `room_id_idx` (`room_id`),
  CONSTRAINT `guest_id` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookingtable`
--

LOCK TABLES `bookingtable` WRITE;
/*!40000 ALTER TABLE `bookingtable` DISABLE KEYS */;
INSERT INTO `bookingtable` VALUES (7,8,5,'2022-09-01','2022-10-01'),(8,5,6,'2022-09-02','2022-10-05'),(9,1,7,'2023-10-02','2023-10-10'),(10,4,8,'2023-10-02','2023-10-10'),(11,5,9,'2023-09-02','2023-10-10'),(12,7,10,'2023-09-02','2023-10-10'),(13,8,11,'2023-09-02','2023-10-10'),(14,11,12,'2023-10-02','2023-10-10'),(18,1,16,'2023-09-15','2023-09-29'),(20,4,17,'2023-09-21','2023-09-30');
/*!40000 ALTER TABLE `bookingtable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guest`
--

DROP TABLE IF EXISTS `guest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(45) NOT NULL,
  `passport` varchar(45) NOT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  `notes` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `personid_UNIQUE` (`id`),
  UNIQUE KEY `passport_UNIQUE` (`passport`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest`
--

LOCK TABLES `guest` WRITE;
/*!40000 ALTER TABLE `guest` DISABLE KEYS */;
INSERT INTO `guest` VALUES (5,'Sam','FF365987','(0)000000058','sam6@gmail.com','past','blocked'),(6,'John','FF36590','(0)000000000','sam5@gmail.com','future',''),(7,'Jane','xx00001',NULL,'sam4@gmail.com','future',NULL),(8,'Ken','xx00000',NULL,'sam3@gmail.com','future',NULL),(9,'Mike Jordan','FF3659',NULL,'sam8@gmail.com','current',NULL),(10,'Faust','FF36591',NULL,'','current',NULL),(11,'CSS','FF36592',NULL,'sam1@gmail.com','current',NULL),(12,'HTML','FF36593',NULL,'sam2@gmail.com','past',NULL),(16,'John','232312','(0)000000000','jane@gmail.com','future',''),(17,'John','12312311','(0)000000000','sam5@gmail.com','future','');
/*!40000 ALTER TABLE `guest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_number` varchar(45) NOT NULL,
  `class_type` varchar(45) NOT NULL,
  `price` double NOT NULL,
  `phone_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `room_number_UNIQUE` (`room_number`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'586','cheap',100.2,'101'),(3,'209','expensive',258,'234'),(4,'202','medium',180.3,'202'),(5,'203','expensive',270.3,'203'),(6,'204','medium',194.65,'204'),(7,'103','cheap',115.3,'103'),(8,'104','cheap',112.35,'104'),(9,'205','medium',165.6,'205'),(10,'301','luxury',350.6,'301'),(11,'401','luxury',370.68,'401'),(13,'598','cheap',165.6,'12565');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-10 14:28:06
