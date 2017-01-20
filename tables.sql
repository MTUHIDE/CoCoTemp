CREATE TABLE data
(
  id          BINARY(16) PRIMARY KEY             NOT NULL,
  user_id     INT(11)                            NOT NULL,
  device_id   BINARY(16)                         NOT NULL,
  date        DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  temperature DOUBLE                             NOT NULL
);
CREATE TABLE device
(
  id               BINARY(16) PRIMARY KEY NOT NULL,
  user_id          INT(11)                NOT NULL,
  device_name      TEXT,
  device_latitude  DOUBLE(10, 8),
  device_longitude DECIMAL(11, 8)         NOT NULL
);
CREATE TABLE role
(
  id   INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(45)
);
CREATE TABLE upload_history
(
  id          BINARY(16) PRIMARY KEY NOT NULL,
  device_id   BINARY(16),
  date        DATETIME   DEFAULT CURRENT_TIMESTAMP,
  duration    MEDIUMTEXT,
  description TEXT,
  error       TINYINT(1) DEFAULT '0',
  viewed      TINYINT(1) DEFAULT '0'
);
CREATE TABLE user
(
  id       INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(255),
  password VARCHAR(255)
);
CREATE TABLE user_role
(
  user_id INT(11) NOT NULL,
  role_id INT(11) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (user_id, role_id)
);
ALTER TABLE data
  ADD FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE data
  ADD FOREIGN KEY (device_id) REFERENCES device (id);
CREATE INDEX data_device_id_fk
  ON data (device_id);
CREATE INDEX data_user_id_fk
  ON data (user_id);
ALTER TABLE device
  ADD FOREIGN KEY (user_id) REFERENCES user (id);
CREATE INDEX device_user_id_fk
  ON device (user_id);
ALTER TABLE upload_history
  ADD FOREIGN KEY (device_id) REFERENCES device (id);
CREATE INDEX upload_history_device_id_fk
  ON upload_history (device_id);
CREATE UNIQUE INDEX upload_history_id_uindex
  ON upload_history (id);
ALTER TABLE user_role
  ADD FOREIGN KEY (user_id) REFERENCES user (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE user_role
  ADD FOREIGN KEY (role_id) REFERENCES role (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX fk_user_role_roleid_idx
  ON user_role (role_id);