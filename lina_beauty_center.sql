-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 21 May 2026, 21:36:03
-- Sunucu sürümü: 10.4.32-MariaDB
-- PHP Sürümü: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `lina_beauty_center`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `adminler`
--

CREATE TABLE `adminler` (
  `id` int(11) NOT NULL,
  `kullanici_adi` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `sifre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `adminler`
--

INSERT INTO `adminler` (`id`, `kullanici_adi`, `email`, `sifre`) VALUES
(1, 'admin', 'linabeautygelisim@gmail.com', '123456');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `calisanlar`
--

CREATE TABLE `calisanlar` (
  `id` int(11) NOT NULL,
  `ad_soyad` varchar(100) NOT NULL,
  `uzmanlik_alani` varchar(100) DEFAULT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `durum` varchar(20) DEFAULT 'Aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `calisanlar`
--

INSERT INTO `calisanlar` (`id`, `ad_soyad`, `uzmanlik_alani`, `telefon`, `email`, `durum`) VALUES
(1, 'Uzman Hayal', 'Cilt Bakımı', '0555 111 11 11', 'linabeautygelisim@gmail.com', 'Aktif'),
(2, 'Uzman Aleyna', 'Lazer Epilasyon', '0555 222 22 22', 'linabeautygelisim@gmail.com', 'Aktif'),
(3, 'Uzman Yağmur', 'Kaş & Kirpik', '0555 333 33 33', 'linabeautygelisim@gmail.com', 'Aktif'),
(4, 'Uzman Elif', 'Protez Tırnak', '0555 444 44 44', 'linabeautygelisim@gmail.com', 'Aktif'),
(5, 'Uzman Zeynep', 'Bölgesel İncelme', '0555 555 55 55', 'linabeautygelisim@gmail.com', 'Aktif'),
(6, 'Uzman Aylin', 'Mezoterapi', '0555 666 66 66', 'linabeautygelisim@gmail.com', 'Aktif');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `hizmetler`
--

CREATE TABLE `hizmetler` (
  `id` int(11) NOT NULL,
  `hizmet_adi` varchar(100) NOT NULL,
  `kategori` varchar(100) DEFAULT NULL,
  `sure` varchar(30) DEFAULT NULL,
  `fiyat` decimal(10,2) DEFAULT NULL,
  `durum` varchar(20) DEFAULT 'Aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `hizmetler`
--

INSERT INTO `hizmetler` (`id`, `hizmet_adi`, `kategori`, `sure`, `fiyat`, `durum`) VALUES
(1, 'Cilt Bakımı', 'Cilt Bakımı', '60 dk.', 4000.00, 'Aktif'),
(2, 'Lazer Epilasyon', 'Lazer', '45 dk.', 15000.00, 'Aktif'),
(3, 'Kaş & Kirpik', 'Kaş & Kirpik', '40 dk.', 5000.00, 'Aktif'),
(4, 'Protez Tırnak', 'Tırnak', '90 dk.', 3000.00, 'Aktif'),
(5, 'Bölgesel İncelme', 'Bölgesel İncelme', '75 dk.', 16000.00, 'Aktif'),
(6, 'Mezoterapi', 'Mezoterapi', '60 dk.', 12000.00, 'Aktif');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `mesajlar`
--

CREATE TABLE `mesajlar` (
  `id` int(11) NOT NULL,
  `musteri_id` int(11) DEFAULT NULL,
  `gonderen_tipi` varchar(20) NOT NULL,
  `mesaj` text NOT NULL,
  `tarih` timestamp NOT NULL DEFAULT current_timestamp(),
  `okundu` tinyint(1) DEFAULT 0,
  `arsiv` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `musteriler`
--

CREATE TABLE `musteriler` (
  `id` int(11) NOT NULL,
  `ad_soyad` varchar(100) NOT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `kullanici_adi` varchar(50) NOT NULL,
  `sifre` varchar(100) NOT NULL,
  `dogum_tarihi` date DEFAULT NULL,
  `cinsiyet` varchar(20) DEFAULT NULL,
  `adres` text DEFAULT NULL,
  `durum` varchar(20) DEFAULT 'Aktif',
  `kayit_tarihi` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `randevular`
--

CREATE TABLE `randevular` (
  `id` int(11) NOT NULL,
  `musteri_id` int(11) DEFAULT NULL,
  `ad_soyad` varchar(100) DEFAULT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `hizmet` varchar(100) DEFAULT NULL,
  `uzman` varchar(100) DEFAULT NULL,
  `tarih` date DEFAULT NULL,
  `saat` varchar(10) DEFAULT NULL,
  `notunuz` text DEFAULT NULL,
  `durum` varchar(20) DEFAULT 'Beklemede',
  `olusturma_tarihi` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `adminler`
--
ALTER TABLE `adminler`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kullanici_adi` (`kullanici_adi`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Tablo için indeksler `calisanlar`
--
ALTER TABLE `calisanlar`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `hizmetler`
--
ALTER TABLE `hizmetler`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `mesajlar`
--
ALTER TABLE `mesajlar`
  ADD PRIMARY KEY (`id`),
  ADD KEY `musteri_id` (`musteri_id`);

--
-- Tablo için indeksler `musteriler`
--
ALTER TABLE `musteriler`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `kullanici_adi` (`kullanici_adi`);

--
-- Tablo için indeksler `randevular`
--
ALTER TABLE `randevular`
  ADD PRIMARY KEY (`id`),
  ADD KEY `musteri_id` (`musteri_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `adminler`
--
ALTER TABLE `adminler`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Tablo için AUTO_INCREMENT değeri `calisanlar`
--
ALTER TABLE `calisanlar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Tablo için AUTO_INCREMENT değeri `hizmetler`
--
ALTER TABLE `hizmetler`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Tablo için AUTO_INCREMENT değeri `mesajlar`
--
ALTER TABLE `mesajlar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Tablo için AUTO_INCREMENT değeri `musteriler`
--
ALTER TABLE `musteriler`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Tablo için AUTO_INCREMENT değeri `randevular`
--
ALTER TABLE `randevular`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `mesajlar`
--
ALTER TABLE `mesajlar`
  ADD CONSTRAINT `mesajlar_ibfk_1` FOREIGN KEY (`musteri_id`) REFERENCES `musteriler` (`id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `randevular`
--
ALTER TABLE `randevular`
  ADD CONSTRAINT `randevular_ibfk_1` FOREIGN KEY (`musteri_id`) REFERENCES `musteriler` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
