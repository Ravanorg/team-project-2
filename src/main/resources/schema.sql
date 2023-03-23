CREATE TABLE IF NOT EXISTS Student (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Customer(
    id varchar(10) primary key ,
    name varchar(50) not null ,
    address varchar(100)not null
)
CREATE TABLE IF NOT EXISTS Employee(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(200) NOT NULL
);
CREATE TABLE IF NOT EXISTS Teacher(
    id VARCHAR(10) PRIMARY KEY ,
    name VARCHAR(100) NOT NULL ,
    address VARCHAR(200) NOT NULL
)
