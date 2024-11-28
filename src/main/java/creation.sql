/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  alberto
 * Created: Nov 22, 2024
 */

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 27, 2024 at 10:16 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `FornoDourado`
--
CREATE DATABASE IF NOT EXISTS `FornoDourado` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `FornoDourado`;

-- --------------------------------------------------------

--
-- Table structure for table `funcionarios`
--

CREATE TABLE IF NOT EXISTS `funcionarios` (
  `CPF` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(120) NOT NULL,
  `CARGO` varchar(10) NOT NULL,
  `USUARIO` varchar(25) NOT NULL,
  `STATUS` tinyint(1) NOT NULL,
  PRIMARY KEY (`CPF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pedidos`
--

CREATE TABLE IF NOT EXISTS `pedidos` (
  `CODPED` int(11) NOT NULL AUTO_INCREMENT,
  `DATAPED` date NOT NULL,
  `PGTO` enum('DEBITO','CREDITO','PIX','BOLETO') NOT NULL,
  PRIMARY KEY (`CODPED`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pedidositem`
--

CREATE TABLE IF NOT EXISTS `pedidositem` (
  `CODPED` int(11) NOT NULL,
  `CODPROD` int(11) NOT NULL,
  `QTD` int(11) NOT NULL,
  KEY `CODPED` (`CODPED`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `produtos`
--

CREATE TABLE IF NOT EXISTS `produtos` (
  `CODPROD` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(50) NOT NULL,
  `VALUNI` double NOT NULL DEFAULT 0,
  `IMG` blob DEFAULT NULL,
  PRIMARY KEY (`CODPROD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `shadow`
--

CREATE TABLE IF NOT EXISTS `shadow` (
  `CODFUN` int(11) NOT NULL AUTO_INCREMENT,
  `HASH` varchar(128) NOT NULL,
  `SALT` varchar(128) NOT NULL,
  PRIMARY KEY (`CODFUN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pedidositem`
--
ALTER TABLE `pedidositem`
  ADD CONSTRAINT `pedidositem_ibfk_1` FOREIGN KEY (`CODPED`) REFERENCES `pedidos` (`CODPED`);
COMMIT;