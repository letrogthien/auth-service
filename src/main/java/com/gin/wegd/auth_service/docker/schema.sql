-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
                                     id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE'),
    date_of_birth DATE,
    status ENUM('ACTIVE', 'INACTIVE', 'BANED'),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    2fa_enabled BOOLEAN DEFAULT FALSE,
    );

-- Tạo bảng user_roles (cho danh sách Role trong User)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(36),
    role ENUM('USER', 'ADMIN', 'MODERATOR'),
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Tạo bảng instance_message (quan hệ 1-1 với users)
CREATE TABLE IF NOT EXISTS instance_message (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) UNIQUE, -- Đảm bảo mỗi user chỉ có 1 instance_message
    instance_message ENUM('ZALO', 'INSTAGRAM'),
    data VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Tạo bảng address (quan hệ 1-1 với users)
CREATE TABLE IF NOT EXISTS address (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) UNIQUE, -- Đảm bảo mỗi user chỉ có 1 address
    address1 VARCHAR(255),
    address2 VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Tạo bảng user_phone (quan hệ 1-1 với users)
CREATE TABLE IF NOT EXISTS user_phone (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) UNIQUE, -- Đảm bảo mỗi user chỉ có 1 user_phone
    phone VARCHAR(20),
    active BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Tạo bảng otp
CREATE TABLE IF NOT EXISTS otp (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    username VARCHAR(255),
    email VARCHAR(255),
    created_at DATETIME,
    expired_at DATETIME,
    otp VARCHAR(255) NOT NULL,
    otp_purpose ENUM('REGISTRATION', 'PASSWORD_RESET', 'VERIFICATION'),
    active BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Tạo bảng jwt
CREATE TABLE IF NOT EXISTS jwt (
    id VARCHAR(36) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id VARCHAR(36),
    status ENUM('ACTIVE', 'EXPIRED', 'REVOKED') NOT NULL,
    token_type ENUM('BEARER', 'ONE_TIME_TOKEN') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Tạo bảng error
CREATE TABLE IF NOT EXISTS error (
    id VARCHAR(36) PRIMARY KEY,
    message TEXT NOT NULL,
    timestamp DATETIME NOT NULL
    );