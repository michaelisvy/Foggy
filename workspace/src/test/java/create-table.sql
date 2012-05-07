CREATE TABLE `city` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

CREATE TABLE `RawMessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `twitterMessageId` varchar(30) DEFAULT NULL,
  `message` varchar(200) NOT NULL,
  `date` datetime DEFAULT NULL,
  `cityId` int(3) DEFAULT NULL,
  `processed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cityId` (`cityId`),
  CONSTRAINT `rawmessage_ibfk_1` FOREIGN KEY (`cityId`) REFERENCES `City` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=806 DEFAULT CHARSET=latin1;

CREATE TABLE `AirData` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `fineParticleIndex` float DEFAULT NULL,
  `airQualityIndex` int(1) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `date` datetime DEFAULT NULL,
  `rawMessageId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `rawMessageId` (`rawMessageId`),
  CONSTRAINT `airdata_ibfk_1` FOREIGN KEY (`rawMessageId`) REFERENCES `RawMessage` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=latin1;

INSERT INTO `city` (`id`, `name`)
VALUES
	(1, 'Beijing'),
	(2, 'Guangzhou');

INSERT INTO `RawMessage` (`id`, `twitterMessageId`, `message`, `date`, `cityId`, `processed`)
VALUES
	(1, '123627748244721664', '10-11-2011; 13:00; PM2.5; 109.0; 176; Unhealthy // Ozone; 3.6; 3; Good', '2011-10-11 13:15:26', 1, 1),
	(2, '123642848611536896', '10-11-2011; 14:00; PM2.5; 139.0; 193; Unhealthy // Ozone; 7.3; 6; Good', '2011-10-11 14:15:26', 1, 1),
	(3, '123657974135795712', '10-11-2011; 15:00; PM2.5; 139.0; 193; Unhealthy // Ozone; 12.4; 10; Good', '2011-10-11 15:15:33', 1, 1),
	(4, '123673066113990656', '10-11-2011; 16:00; PM2.5; 132.0; 189; Unhealthy // Ozone; 17.9; 15; Good', '2011-10-11 16:15:31', 1, 1);

INSERT INTO `RawMessage` (`id`, `twitterMessageId`, `message`, `date`, `cityId`, `processed`)
VALUES
	(416, '127190956679241728', '10-21-2011; 09:00; PM2.5; 73; 155; Unhealthy', '2011-10-21 09:14:21', 2, 1),
	(417, '127280595750486016', '10-21-2011; 15:00; PM2.5; 72; 155; Unhealthy', '2011-10-21 15:10:33', 2, 1),
	(418, '128281261117685760', '10-24-2011; 09:00; PM2.5; 100; 171; Unhealthy', '2011-10-24 09:26:50', 2, 1),
	(419, '128353895016693760', '10-24-2011; 14:00; PM2.5; 140; 194; Unhealthy', '2011-10-24 14:15:27', 2, 1);

