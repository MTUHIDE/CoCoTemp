CREATE TABLE data
(
  id          BINARY(16) PRIMARY KEY             NOT NULL,
  user_id     INT                                NOT NULL,
  device_id   BINARY(16)                         NOT NULL,
  date        DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  temperature DOUBLE                             NOT NULL,
  CONSTRAINT data_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id),
  CONSTRAINT data_ibfk_2 FOREIGN KEY (device_id) REFERENCES device (id)
);
CREATE TABLE device
(
  id               BINARY(16) PRIMARY KEY NOT NULL,
  user_id          INT                    NOT NULL,
  device_name      LONGTEXT,
  device_latitude  DOUBLE(10, 8),
  device_longitude DECIMAL(11, 8)         NOT NULL,
  CONSTRAINT device_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE role
(
  id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(45)
);
CREATE TABLE upload_history
(
  id          BINARY(16) PRIMARY KEY NOT NULL,
  device_id   BINARY(16),
  date        DATETIME DEFAULT CURRENT_TIMESTAMP,
  duration    LONGTEXT,
  description LONGTEXT,
  error       TINYINT  DEFAULT 0,
  viewed      TINYINT  DEFAULT 0,
  user_id     INT,
  records     INT DEFAULT 0          NOT NULL,
  CONSTRAINT upload_history_ibfk_1 FOREIGN KEY (device_id) REFERENCES device (id),
  CONSTRAINT upload_history_user_id_fk FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE user
(
  id             INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username       VARCHAR(255),
  password       VARCHAR(255),
  first_name     LONGTEXT,
  middle_initial LONGTEXT,
  last_name      LONGTEXT,
  email          LONGTEXT        NOT NULL
);
CREATE TABLE user_role
(
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_role_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT user_role_ibfk_2 FOREIGN KEY (role_id) REFERENCES role (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);