CREATE TABLE IF NOT EXISTS Student (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(100) NOT NULL
);

INSERT INTO Student (id, name, address) VALUES
                                            ('S001','Vishwa','Beliatta'),
                                            ('S002','Priman','Tangalle');