CREATE DATABASE IF NOT EXISTS ecs_sync
  DEFAULT CHARSET = utf8mb4
  DEFAULT COLLATE = utf8mb4_bin;
CREATE USER IF NOT EXISTS 'ecssync'@'%'
  IDENTIFIED BY PASSWORD '*5AF3FEB858EE42E14B1E9E482815C4BFCAAC2F04';
GRANT ALL ON ecs_sync.* TO 'ecssync'@'%';
GRANT FILE ON *.* TO 'ecssync'@'%';
FLUSH PRIVILEGES;
