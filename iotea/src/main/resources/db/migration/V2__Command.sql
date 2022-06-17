CREATE TABLE commands (
	id INT AUTO_INCREMENT NOT NULL,
	name VARCHAR(255) UNIQUE NOT NULL,
  template TEXT NOT NULL DEFAULT '',
  note TEXT NOT NULL DEFAULT '',
  CONSTRAINT pk_commands PRIMARY KEY (id)
);