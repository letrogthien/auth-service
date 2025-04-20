CREATE TABLE IF NOT EXISTS users (
                                     id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE'),
    date_of_birth DATE,
    status ENUM('ACTIVE', 'INACTIVE', 'BANNED'),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    2fa_enabled BOOLEAN DEFAULT FALSE
    );
ALTER TABLE users ADD FULLTEXT(username);

CREATE TABLE IF NOT EXISTS roles (
                                     id BINARY(16) PRIMARY KEY,
    name ENUM('USER', 'ADMIN', 'GUEST', 'MIDDLE_MAN') NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BINARY(16),
    role_id BINARY(16),
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
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
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' ,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS error (
                                     id BINARY(16) PRIMARY KEY,
    message TEXT NOT NULL,
    timestamp DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS delete_kyc_requests (
                                                   id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    verify_img_path VARCHAR(255) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
    );

INSERT IGNORE INTO roles (id, name)
VALUES
    (UUID_TO_BIN(UUID()), 'USER'),
    (UUID_TO_BIN(UUID()), 'ADMIN'),
    (UUID_TO_BIN(UUID()), 'GUEST'),
    (UUID_TO_BIN(UUID()), 'MIDDLE_MAN');