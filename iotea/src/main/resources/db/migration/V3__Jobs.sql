CREATE TABLE jobs (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) UNIQUE NOT NULL,
  device_id INT NOT NULL,
  command_id INT NOT NULL,
  CONSTRAINT pk_jobs PRIMARY KEY (id),
  CONSTRAINT fk_device FOREIGN KEY (device_id) REFERENCES devices (id),
  CONSTRAINT fk_command FOREIGN KEY (command_id) REFERENCES commands (id)
);