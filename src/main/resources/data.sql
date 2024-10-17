-- Insert sample customers
INSERT INTO customer (userId, username, DOB) VALUES
(1, 'John Doe', '1990-01-15'),
(2, 'Jane Smith', '1985-05-23'),
(3, 'Alice Johnson', '1992-07-30');

-- Insert sample transactions with updated column name
INSERT INTO transaction (userId, transDate, amount, reference, status, transactionUser) VALUES
(1, '2024-01-01', 100.50, 'REF123', 'Completed', 'John Doe'),
(1, '2024-01-02', 200.75, 'REF124', 'Completed', 'John Doe'),
(2, '2024-01-01', 150.00, 'REF125', 'Pending', 'Jane Smith'),
(3, '2024-01-03', 300.00, 'REF126', 'Completed', 'Alice Johnson'),
(2, NULL, 400.00, 'REF127', 'Completed', 'Jane Smith'); -- Transaction with NULL date
