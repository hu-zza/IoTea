CREATE TABLE devices (
	id INT AUTO_INCREMENT NOT NULL,
	uid VARCHAR(255) UNIQUE NOT NULL,
  name VARCHAR(255) UNIQUE NOT NULL,
  ip VARCHAR(255) NOT NULL,
  port INT NOT NULL,
  CONSTRAINT pk_devices PRIMARY KEY (id)
);