CREATE DATABASE painelvpn
GO
USE painelvpn
GO
CREATE TABLE users(
	id			INT	IDENTITY(1,1)	PRIMARY KEY,
	username	VARCHAR(30)	UNIQUE	NOT NULL,
	password	VARCHAR(30)			NOT NULL,
	email		VARCHAR(255)		NOT NULL,
	fullname	VARCHAR(255)		NOT NULL,
	role		INT					NOT NULL
);
GO
INSERT INTO users VALUES ('admin', 'admin', 'admin@empresa.com.br', 'admin', 1);
GO
ALTER TABLE users ADD locked BIT NOT NULL DEFAULT 0;
GO
ALTER TABLE users ADD login_attempts INT NOT NULL DEFAULT 0;