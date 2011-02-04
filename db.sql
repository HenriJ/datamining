--
-- Database: 'mom'
--

-- --------------------------------------------------------

DROP TABLE IF EXISTS files;
CREATE TABLE IF NOT EXISTS files (
  id int(11) NOT NULL AUTO_INCREMENT,
  `server` int(11) NOT NULL,
  path tinytext NOT NULL,
  `name` varchar(255) NOT NULL,
  size bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL,
  timechecked datetime NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
