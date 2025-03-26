CREATE TABLE IF NOT EXISTS users (
                                     id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE'),
    date_of_birth DATE,
    status ENUM('ACTIVE', 'INACTIVE', 'BANED'),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    2fa_enabled BOOLEAN DEFAULT FALSE
    );
ALTER TABLE users ADD FULLTEXT(username);
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BINARY(16),
    role ENUM('USER', 'ADMIN', 'MODERATOR'),
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS instance_message (
                                                id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) UNIQUE,
    instance_message ENUM('ZALO', 'INSTAGRAM'),
    data VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS address (
                                       id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) UNIQUE,
    address1 VARCHAR(255),
    address2 VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS user_phone (
                                          id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) UNIQUE,
    phone VARCHAR(20),
    active BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS user_id_documents (
                                                 id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL UNIQUE,
    front_id VARCHAR(255) NOT NULL,
    back_id VARCHAR(255) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS otp (
                                   id BINARY(16) PRIMARY KEY,
    user_id BINARY(16),
    username VARCHAR(255),
    email VARCHAR(255),
    created_at DATETIME,
    expired_at DATETIME,
    otp VARCHAR(255) NOT NULL,
    otp_purpose ENUM('REGISTRATION', 'PASSWORD_RESET', 'VERIFICATION'),
    active BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS error (
                                     id BINARY(16) PRIMARY KEY,
    message TEXT NOT NULL,
    timestamp DATETIME NOT NULL
    );