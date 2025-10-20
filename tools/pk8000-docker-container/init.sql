CREATE DATABASE IF NOT EXISTS catalog_db;
# Пользователь для приложения
CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON catalog_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON catalog_db.* TO 'admin'@'localhost';
CREATE DATABASE IF NOT EXISTS keycloak;
# Логин для Keycloak
CREATE USER 'keycloak'@'%' IDENTIFIED BY 'keycloak';
CREATE USER 'keycloak'@'localhost' IDENTIFIED BY 'keycloak';
GRANT ALL PRIVILEGES ON keycloak.* TO 'keycloak'@'%';
GRANT ALL PRIVILEGES ON keycloak.* TO 'keycloak'@'localhost';
FLUSH PRIVILEGES;

