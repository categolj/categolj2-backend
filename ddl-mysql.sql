CREATE DATABASE  IF NOT EXISTS `categolj2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `categolj2`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS category;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE category (
  category_order int(11) NOT NULL,
  entry_id int(11) NOT NULL,
  category_name varchar(128) NOT NULL,
  PRIMARY KEY (category_order,entry_id),
  KEY FK_ihjisvvo7d8b0vu26asrl0d3a (entry_id),
  CONSTRAINT FK_ihjisvvo7d8b0vu26asrl0d3a FOREIGN KEY (entry_id) REFERENCES entry (entry_id)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS entry;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE entry (
  entry_id int(11) NOT NULL AUTO_INCREMENT,
  version bigint(20) DEFAULT NULL,
  created_by varchar(128) DEFAULT NULL,
  created_date datetime DEFAULT NULL,
  last_modified_by varchar(128) DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  contents longtext NOT NULL,
  `format` varchar(10) NOT NULL,
  published tinyint(1) NOT NULL,
  title longtext NOT NULL,
  PRIMARY KEY (entry_id)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS entry_history;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE entry_history (
  entry_histry_id varchar(36) NOT NULL,
  version bigint(20) DEFAULT NULL,
  created_by varchar(128) DEFAULT NULL,
  created_date datetime DEFAULT NULL,
  last_modified_by varchar(128) DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  contents longtext NOT NULL,
  `format` varchar(10) NOT NULL,
  title longtext NOT NULL,
  entry_id int(11) NOT NULL,
  PRIMARY KEY (entry_histry_id),
  KEY FK_f2mu7h50hhd8dmrkyb01jpch8 (entry_id),
  CONSTRAINT FK_f2mu7h50hhd8dmrkyb01jpch8 FOREIGN KEY (entry_id) REFERENCES entry (entry_id)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS link;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE link (
  url varchar(128) NOT NULL,
  version bigint(20) DEFAULT NULL,
  created_by varchar(128) DEFAULT NULL,
  created_date datetime DEFAULT NULL,
  last_modified_by varchar(128) DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  link_name varchar(128) NOT NULL,
  PRIMARY KEY (url)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS login_history;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE login_history (
  login_histry_id varchar(36) NOT NULL,
  login_agent varchar(128) NOT NULL,
  created_date datetime NOT NULL,
  login_host varchar(128) NOT NULL,
  username varchar(128) NOT NULL,
  PRIMARY KEY (login_histry_id)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS role;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE role (
  role_id int(11) NOT NULL AUTO_INCREMENT,
  version bigint(20) DEFAULT NULL,
  created_by varchar(128) DEFAULT NULL,
  created_date datetime DEFAULT NULL,
  last_modified_by varchar(128) DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  role_name varchar(25) NOT NULL,
  PRIMARY KEY (role_id),
  UNIQUE KEY UK_iubw515ff0ugtm28p8g3myt0h (role_name)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS upload_file;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE upload_file (
  file_id varchar(36) NOT NULL,
  version bigint(20) DEFAULT NULL,
  created_by varchar(128) DEFAULT NULL,
  created_date datetime DEFAULT NULL,
  last_modified_by varchar(128) DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  file_content longblob,
  file_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (file_id)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  username varchar(128) NOT NULL,
  version bigint(20) DEFAULT NULL,
  created_by varchar(128) DEFAULT NULL,
  created_date datetime DEFAULT NULL,
  last_modified_by varchar(128) DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  email varchar(128) NOT NULL,
  enabled tinyint(1) NOT NULL,
  first_name varchar(128) NOT NULL,
  last_name varchar(128) NOT NULL,
  locked tinyint(1) NOT NULL,
  `password` longtext NOT NULL,
  PRIMARY KEY (username)
);
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS user_role;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_role (
  username varchar(128) NOT NULL,
  role_id int(11) NOT NULL,
  PRIMARY KEY (username,role_id),
  KEY FK_it77eq964jhfqtu54081ebtio (role_id),
  KEY FK_aphxiciwirrvuc0y7y2s2rufj (username),
  CONSTRAINT FK_aphxiciwirrvuc0y7y2s2rufj FOREIGN KEY (username) REFERENCES `user` (username),
  CONSTRAINT FK_it77eq964jhfqtu54081ebtio FOREIGN KEY (role_id) REFERENCES role (role_id)
);
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;