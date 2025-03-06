CREATE DATABASE  IF NOT EXISTS `campeonato-skills` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `campeonato-skills`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: campeonato-skills
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `competidor`
--

DROP TABLE IF EXISTS `competidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competidor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `centro` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `nota_total` float DEFAULT '0',
  `especialidades_id_especialidad` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnr2j12gx84td1e8h7kr5qu0qi` (`especialidades_id_especialidad`),
  CONSTRAINT `FKnr2j12gx84td1e8h7kr5qu0qi` FOREIGN KEY (`especialidades_id_especialidad`) REFERENCES `especialidades` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competidor`
--

LOCK TABLES `competidor` WRITE;
/*!40000 ALTER TABLE `competidor` DISABLE KEYS */;
INSERT INTO `competidor` VALUES (14,'I.E.S. Las Fuentezuelas','Manuel Ortiz',0,2),(15,'I.E.S. Las Fuentezuelas','Pablo Tejerina',0,3),(16,'I.E.S. Las Fuentezuelas','Jesús Milla',0,3),(17,'I.E.S. Las Fuentezuelas','Francisco José de la Torre',0,2),(22,'Centro Deportivo XYZ modificado','Juan Pérez modificado',0,2),(23,'Centro Deportivo XYZ','Juan Pérez dos',8,2),(24,'Centro Deportivo XYZ','Juan Pérez tres',4.8,2),(25,'Centro Deportivo XYZ','Juan Pérez borrar',0,2),(26,'prueba modify dos','prueba borrar',7,11);
/*!40000 ALTER TABLE `competidor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `especialidades`
--

DROP TABLE IF EXISTS `especialidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `especialidades` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(3) NOT NULL,
  `nombre` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4eokgmwx2axlpcxkjxnank438` (`codigo`),
  UNIQUE KEY `UKkq918o2plf4a6b25osvl96dj7` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `especialidades`
--

LOCK TABLES `especialidades` WRITE;
/*!40000 ALTER TABLE `especialidades` DISABLE KEYS */;
INSERT INTO `especialidades` VALUES (1,'ADM','Administrador'),(2,'INF','Informática'),(3,'MEC','Mecatrónica'),(4,'DMC','Diseño Mecánico CAD'),(5,'CNC','CNC Fresado'),(6,'SOL','Soldadura'),(7,'FCH','Fontanería y calefacción'),(8,'ELC','Electrónica'),(9,'DWE','Desarrollo web'),(10,'IEE','Instalaciones eléctricas'),(11,'CIN','Control industrial'),(12,'ROM','Robótica móvil'),(13,'EBN','Ebanistería'),(14,'CAR','Carpintería'),(15,'FLO','Floristería'),(16,'PEL','Peluquería'),(17,'EST','Estética'),(18,'TFM','Tecnología de la moda'),(19,'TDA','Tecnología del automóvil'),(21,'SRB','Servicio de Restaurante y bar'),(22,'PDA','Pintura del Automóvil'),(23,'JAP','Jardinería paisajística'),(24,'REF','Refrigeración'),(25,'ASR','Administración de Sistemas en Red (TI)'),(26,'CAE','Cuidados auxiliares de Enfermería y Atención Sociosanitaria'),(27,'EVM','Escaparatismo y Visual Merchandising'),(28,'PYP','Panadería y Pastelería'),(29,'REH','Recepción hotelera'),(49,'asd','modificado ok'),(73,'xxx','modify'),(74,'MMM','prueba GGG'),(75,'qwe','ñjñdfkjañsldkfjñadskljfñlkasdf');
/*!40000 ALTER TABLE `especialidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacion`
--

DROP TABLE IF EXISTS `evaluacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_actual` date NOT NULL,
  `puntuacion_obtenida` float DEFAULT NULL,
  `competidor_idcompetidor` bigint NOT NULL,
  `user_idexperto` bigint DEFAULT NULL,
  `prueba_idprueba` bigint NOT NULL,
  `estado` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK49rox79wwl6x9pwwsj8iw105j` (`competidor_idcompetidor`),
  KEY `FKlvhqu5a8nqprd1vn1as5idrv4` (`prueba_idprueba`),
  KEY `FKl10t7mj7dpwlrxjjktriblcwb` (`user_idexperto`),
  CONSTRAINT `FK49rox79wwl6x9pwwsj8iw105j` FOREIGN KEY (`competidor_idcompetidor`) REFERENCES `competidor` (`id`),
  CONSTRAINT `FKl10t7mj7dpwlrxjjktriblcwb` FOREIGN KEY (`user_idexperto`) REFERENCES `users` (`id`),
  CONSTRAINT `FKlvhqu5a8nqprd1vn1as5idrv4` FOREIGN KEY (`prueba_idprueba`) REFERENCES `prueba` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacion`
--

LOCK TABLES `evaluacion` WRITE;
/*!40000 ALTER TABLE `evaluacion` DISABLE KEYS */;
INSERT INTO `evaluacion` VALUES (19,'2025-02-25',0,14,NULL,27,'pendiente'),(20,'2025-02-25',0,17,NULL,27,'pendiente'),(21,'2025-02-25',0,22,NULL,27,'pendiente'),(22,'2025-02-25',8,23,39,27,'evaluado'),(23,'2025-02-25',4.8,24,39,27,'evaluado'),(24,'2025-02-25',0,25,37,27,'seleccionado');
/*!40000 ALTER TABLE `evaluacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacion_item`
--

DROP TABLE IF EXISTS `evaluacion_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comentario` varchar(150) DEFAULT NULL,
  `valoracion` int DEFAULT NULL,
  `evaluacion_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKobgf0mxr7hu35vfuh9nlp237l` (`evaluacion_id`),
  KEY `FK3u0iqg3mn6k3j3a4qg08un8a7` (`item_id`),
  CONSTRAINT `FK3u0iqg3mn6k3j3a4qg08un8a7` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `FKobgf0mxr7hu35vfuh9nlp237l` FOREIGN KEY (`evaluacion_id`) REFERENCES `evaluacion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacion_item`
--

LOCK TABLES `evaluacion_item` WRITE;
/*!40000 ALTER TABLE `evaluacion_item` DISABLE KEYS */;
INSERT INTO `evaluacion_item` VALUES (19,'',0,19,30),(20,'',0,19,31),(21,'',0,19,32),(22,'',0,20,30),(23,'',0,20,31),(24,'',0,20,32),(25,'',0,21,30),(26,'',0,21,31),(27,'',0,21,32),(28,'',2,22,30),(29,'',1,22,31),(30,'',5,22,32),(31,'esta es la buena finalizado',1,23,30),(32,'claro que si finalizado',1,23,31),(33,'perfecto claro finalizado',3,23,32),(34,'',0,24,30),(35,'',0,24,31),(36,'',0,24,32);
/*!40000 ALTER TABLE `evaluacion_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contenido` varchar(300) NOT NULL,
  `grados_consecuencia` int NOT NULL,
  `peso` int NOT NULL,
  `prueba_idprueba` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe88rllfm4838kjxg76243bx4o` (`prueba_idprueba`),
  CONSTRAINT `FKe88rllfm4838kjxg76243bx4o` FOREIGN KEY (`prueba_idprueba`) REFERENCES `prueba` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'guarda 1 55555555555 ',0,1,9),(2,'guarda 2555555555555',0,3,9),(3,'guarda 3 55555555555',0,2,9),(4,'guarda 1 55555555555 ',4,1,10),(5,'guarda 2555555555555',5,3,10),(6,'guarda 3 55555555555',1,2,10),(9,'guarda 1 transaccion',4,1,12),(10,'guarda 2 transaccion',5,3,12),(11,'guarda 3 transaccion',1,2,12),(12,'sdfasdfadsfadsfdasfadsfdasfads',0,0,14),(15,'sadfadsfadsfadsfa',4,2,16),(16,'fadsfadgahgdsfads',2,1,16),(17,'fasdfadsfadsfadsfadsf',6,5,17),(18,'asdfasdfasdfasdfsd',3,1,18),(23,'primer item',5,3,20),(24,'segundo item',1,1,20),(25,'tercer item',3,2,20),(26,'cuarto item',3,3,20),(27,'dos item nuevo',1,3,21),(28,'dos item nuevo dos',5,3,21),(29,'asdfadsfasdflkñadsjñ sdlf ñadsknlf',1,3,26),(30,'item uno evaluar',2,4,27),(31,'item dos evaluar',1,1,27),(32,'item tres evaluar',5,3,27);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prueba`
--

DROP TABLE IF EXISTS `prueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prueba` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archivo` varchar(255) DEFAULT NULL,
  `enunciado` varchar(300) NOT NULL,
  `puntuacion_maxima` int DEFAULT NULL,
  `especialidades_id_especialidad` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk8p07lcraqobioqpcsver8vwu` (`especialidades_id_especialidad`),
  CONSTRAINT `FKk8p07lcraqobioqpcsver8vwu` FOREIGN KEY (`especialidades_id_especialidad`) REFERENCES `especialidades` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prueba`
--

LOCK TABLES `prueba` WRITE;
/*!40000 ALTER TABLE `prueba` DISABLE KEYS */;
INSERT INTO `prueba` VALUES (3,'.\\archivos\\pruebas\\3.pdf','nueva',6,2),(6,'.\\archivos\\pruebas\\6.pdf','se guarda',6,2),(7,'.\\archivos\\pruebas\\7.pdf','se guarda',6,2),(8,'.\\archivos\\pruebas\\8.pdf','se guarda',6,2),(9,'.\\archivos\\pruebas\\9.pdf','se guarda',6,2),(10,'.\\archivos\\pruebas\\10.pdf','se guarda',6,2),(12,'.\\archivos\\pruebas\\12.pdf','transaccion prueba',6,2),(14,'.\\archivos\\pruebas\\14.pdf','afasdfasdfasdfasdf',0,2),(16,'.\\archivos\\pruebas\\16.pdf','ñkjñadfjsfñadsjf`´nadsfnkads´fpknads´knflsad',3,2),(17,'.\\archivos\\pruebas\\17.pdf','ffsdadsfasd sadf asd  s',5,2),(18,'.\\archivos\\pruebas\\18.pdf','fvvfadsfsadfasd',1,2),(20,'.\\archivos\\pruebas\\20.pdf','prueba con evaluaciones automaticas',9,2),(21,'.\\archivos\\pruebas\\21.pdf','prueba dos',6,2),(26,'.\\archivos\\pruebas\\26.pdf','hola aqui la prueba',3,2),(27,'.\\archivos\\pruebas\\27.pdf','nueva prueba xxxxxxx',8,2);
/*!40000 ALTER TABLE `prueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `rol` varchar(45) NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `especialidades_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeuokmiqle2n7d74s373a945xs` (`usuario`),
  KEY `FKousjxbi1afxjxe12etumh3lh7` (`especialidades_id`),
  CONSTRAINT `FKousjxbi1afxjxe12etumh3lh7` FOREIGN KEY (`especialidades_id`) REFERENCES `especialidades` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Admin','$2a$10$aiuRVfO2KpWCRetVBOnNpe.hNyuHhOSx2w/HkL33EscteyCjRgr8K','ROLE_ADMIN','Admin',1),(22,'Fran','$2a$10$xHWtS/nvUNwp54MctrdSCOPzpFLjVhvl5gh/o6mcLEzBWGVohjhVW','ROLE_EXPERTO','fran1@fran.es',2),(23,'Fran','$2a$10$RGw18G7XtswenxbKbEYdC.9WxCFSYHb1Ym.U5u9AZ6ABJAOp6bNea','ROLE_EXPERTO','fran2@fran.es',2),(24,'Fran','$2a$10$hA2x1S0.q.Uf5Dwluyb0sulRLGx8vS/i58O6ieavCQ7ahPkR7BvkG','ROLE_EXPERTO','fran3@fran.es',2),(25,'Fran','$2a$10$rsULhJIpDkpClxk3Xznav.4.xsg00E3llxx8.Thw1/yd7APB40hLe','ROLE_EXPERTO','fran4@fran.es',2),(26,'Fran','$2a$10$.TykYOFfyYPy2hpwwHtQw.xFswCmcg7zgO5/oPbTR7oyPihQt.yuC','ROLE_EXPERTO','fran5@fran.es',3),(29,'Fran modify','$2a$10$cUtfIw5kZcXxnCREG3p1OeCIuCIVd1EnvmBUkQb8VgJgXYFoHPyD2','ROLE_EXPERTO','fran99@fran.es',11),(30,'prueba','$2a$10$s.d8OhPXWVeLQXuYZsRv3.wI/om3j4eX8ORF/9E9nebUM3YXuztHW','ROLE_EXPERTO','prueba@prueba.es',15),(37,'789','$2a$10$JTgEC2d2bdsb/A.plD0zO.tLBa7xwwiO6qRUCkdwjtwzhjeTrfNYK','ROLE_EXPERTO','789',2),(38,'999','$2a$10$6QaVfEuG5XOcVLQBrQjq.eCJmcx7NzL9WRZNd23FTJ4VxnkKvJpgu','ROLE_EXPERTO','999',28),(39,'888','$2a$10$T62kQhpeRPzvepCbs2FmAOfz3Db6OYXQ8ireMDa5t1bM6ylayDzdu','ROLE_EXPERTO','888',2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-26 18:13:05
