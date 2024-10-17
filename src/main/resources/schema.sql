-- Create customer table
CREATE TABLE customer (
    userId INT PRIMARY KEY,
    username VARCHAR(50),
    DOB DATE
);

-- Create transaction table with renamed user column
CREATE TABLE transaction (
    transId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    transDate DATE,
    amount DECIMAL(10, 2),
    reference VARCHAR(50),
    status VARCHAR(20),
    transactionUser VARCHAR(50),  -- Renamed column
    FOREIGN KEY (userId) REFERENCES customer(userId)
);
