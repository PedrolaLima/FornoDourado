-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 02, 2024 at 02:11 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `FornoDourado`
--

-- --------------------------------------------------------

--
-- Table structure for table `funcionarios`
--

CREATE TABLE `funcionarios` (
  `CPF` varchar(14) NOT NULL,
  `NOME` varchar(120) NOT NULL,
  `NASC` date NOT NULL,
  `CARGO` varchar(14) NOT NULL,
  `EMAIL` varchar(25) NOT NULL,
  `CEP` varchar(9) NOT NULL,
  `ENDERECO` varchar(128) NOT NULL,
  `CIDADE` varchar(64) DEFAULT NULL,
  `UF` varchar(2) DEFAULT NULL,
  `STATUS` tinyint(1) NOT NULL,
  `IMG` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `funcionarios`
--

INSERT INTO `funcionarios` (`CPF`, `NOME`, `NASC`, `CARGO`, `EMAIL`, `CEP`, `ENDERECO`, `CIDADE`, `UF`, `STATUS`, `IMG`) VALUES
('098.765.543-43', 'Serena Martinez', '2024-12-12', 'Cozinha', 'sema@padaria.com', '01002-902', 'Rua Direita, 191 ', 'São Paulo', 'SP', 1, NULL),
('123.456.789-01', 'admin', '0000-00-00', 'Administração', 'admin@email.net', '00000-000', 'Alameda do codigo,1001', 'Bugopolis', 'SP', 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pedidos`
--

CREATE TABLE `pedidos` (
  `CODPED` int(11) NOT NULL,
  `DATAPED` date NOT NULL,
  `PGTO` enum('DINHEIRO','DEBITO','CREDITO','PIX') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pedidositem`
--

CREATE TABLE `pedidositem` (
  `CODPED` int(11) NOT NULL,
  `CODPROD` int(11) NOT NULL,
  `QTD` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `produtos`
--

CREATE TABLE `produtos` (
  `CODPROD` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `VALUNI` double NOT NULL DEFAULT 0,
  `DISP` tinyint(1) NOT NULL,
  `IMG` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produtos`
--

INSERT INTO `produtos` (`CODPROD`, `NOME`, `VALUNI`, `DISP`, `IMG`) VALUES
(124, 'Brioche', 13.9, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `shadow`
--

CREATE TABLE `shadow` (
  `CPF` varchar(14) NOT NULL,
  `HASH` varchar(32) NOT NULL,
  `SALT` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shadow`
--

INSERT INTO `shadow` (`CPF`, `HASH`, `SALT`) VALUES
('123.456.789-01', '7865c739c16d5086ba8807d52f680c41', 'd05532b807d6580a54ba3fd3b23f635e');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `funcionarios`
--
ALTER TABLE `funcionarios`
  ADD PRIMARY KEY (`CPF`);

--
-- Indexes for table `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`CODPED`);

--
-- Indexes for table `pedidositem`
--
ALTER TABLE `pedidositem`
  ADD KEY `CODPED` (`CODPED`);

--
-- Indexes for table `produtos`
--
ALTER TABLE `produtos`
  ADD PRIMARY KEY (`CODPROD`);

--
-- Indexes for table `shadow`
--
ALTER TABLE `shadow`
  ADD PRIMARY KEY (`CPF`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `CODPED` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `produtos`
--
ALTER TABLE `produtos`
  MODIFY `CODPROD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=125;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pedidositem`
--
ALTER TABLE `pedidositem`
  ADD CONSTRAINT `pedidositem_ibfk_1` FOREIGN KEY (`CODPED`) REFERENCES `pedidos` (`CODPED`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
