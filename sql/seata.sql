-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: seata
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `branch_table`
--

DROP TABLE IF EXISTS `branch_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch_table` (
                                `branch_id` bigint NOT NULL,
                                `xid` varchar(128) NOT NULL,
                                `transaction_id` bigint DEFAULT NULL,
                                `resource_group_id` varchar(32) DEFAULT NULL,
                                `resource_id` varchar(256) DEFAULT NULL,
                                `lock_key` varchar(128) DEFAULT NULL,
                                `branch_type` varchar(8) DEFAULT NULL,
                                `status` tinyint DEFAULT NULL,
                                `client_id` varchar(64) DEFAULT NULL,
                                `application_data` varchar(2000) DEFAULT NULL,
                                `gmt_create` datetime DEFAULT NULL,
                                `gmt_modified` datetime DEFAULT NULL,
                                PRIMARY KEY (`branch_id`),
                                KEY `idx_xid` (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch_table`
--

LOCK TABLES `branch_table` WRITE;
/*!40000 ALTER TABLE `branch_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `branch_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `distributed_lock`
--

DROP TABLE IF EXISTS `distributed_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `distributed_lock` (
                                    `lock_key` char(20) NOT NULL,
                                    `lock_value` varchar(20) NOT NULL,
                                    `expire` bigint DEFAULT NULL,
                                    `status` int DEFAULT NULL,
                                    PRIMARY KEY (`lock_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `distributed_lock`
--

LOCK TABLES `distributed_lock` WRITE;
/*!40000 ALTER TABLE `distributed_lock` DISABLE KEYS */;
INSERT INTO `distributed_lock` VALUES ('AsyncCommitting',' ',0,NULL),('RetryCommitting',' ',0,NULL),('RetryRollbacking',' ',0,NULL),('TxTimeoutCheck',' ',0,NULL),('UndologDelete',' ',0,NULL);
/*!40000 ALTER TABLE `distributed_lock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_table`
--

DROP TABLE IF EXISTS `global_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `global_table` (
                                `xid` varchar(128) NOT NULL,
                                `transaction_id` bigint DEFAULT NULL,
                                `status` tinyint NOT NULL,
                                `application_id` varchar(32) DEFAULT NULL,
                                `transaction_service_group` varchar(32) DEFAULT NULL,
                                `transaction_name` varchar(128) DEFAULT NULL,
                                `timeout` int DEFAULT NULL,
                                `begin_time` bigint DEFAULT NULL,
                                `application_data` varchar(2000) DEFAULT NULL,
                                `gmt_create` datetime DEFAULT NULL,
                                `gmt_modified` datetime DEFAULT NULL,
                                PRIMARY KEY (`xid`),
                                KEY `idx_gmt_modified_status` (`gmt_modified`,`status`),
                                KEY `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_table`
--

LOCK TABLES `global_table` WRITE;
/*!40000 ALTER TABLE `global_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lock_table`
--

DROP TABLE IF EXISTS `lock_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lock_table` (
                              `row_key` varchar(128) NOT NULL,
                              `xid` varchar(96) DEFAULT NULL,
                              `transaction_id` mediumtext,
                              `branch_id` mediumtext,
                              `resource_id` varchar(256) DEFAULT NULL,
                              `table_name` varchar(32) DEFAULT NULL,
                              `pk` varchar(36) DEFAULT NULL,
                              `gmt_create` datetime DEFAULT NULL,
                              `gmt_modified` datetime DEFAULT NULL,
                              `status` int DEFAULT NULL,
                              PRIMARY KEY (`row_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lock_table`
--

LOCK TABLES `lock_table` WRITE;
/*!40000 ALTER TABLE `lock_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `lock_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-29 18:53:53
