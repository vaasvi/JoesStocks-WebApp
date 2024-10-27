CREATE SCHEMA `JoeStocks` ;
USE JoeStocks;
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 50000.00
);

CREATE TABLE Portfolio (
    trade_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(10) NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    numStock INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (username) REFERENCES Users(username)
);


