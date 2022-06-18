CREATE TABLE jobs (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) UNIQUE NOT NULL,
  CONSTRAINT pk_jobs PRIMARY KEY (id)
);

CREATE TABLE job_device (
  job_id INT NOT NULL,
  device_id INT NOT NULL,
  CONSTRAINT pk_job_device PRIMARY KEY (job_id, device_id),
  CONSTRAINT fk_job FOREIGN KEY (job_id),
  CONSTRAINT fk_device FOREIGN KEY (device_id)
);

CREATE TABLE job_command (
  job_id INT NOT NULL,
  command_id INT NOT NULL,
  CONSTRAINT pk_job_command PRIMARY KEY (job_id, command_id),
  CONSTRAINT fk_job FOREIGN KEY (job_id),
  CONSTRAINT fk_command FOREIGN KEY (command_id)
);
