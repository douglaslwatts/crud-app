CREATE TABLE person (
    person_id integer PRIMARY KEY AUTO_INCREMENT,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email_address varchar(50) NOT NULL,
    street_address varchar(50) NOT NULL,
    city varchar(50) NOT NULL,
    state varchar(2) NOT NULL,
    zip_code varchar(5) NOT NULL
);

CREATE TABLE client (
    client_id      INTEGER     AUTO_INCREMENT PRIMARY KEY,
    company_name   VARCHAR(100) NOT NULL,
    website        VARCHAR(255) NOT NULL,
    phone          VARCHAR(20) NOT NULL,
    street_address VARCHAR(50) NOT NULL,
    city           VARCHAR(50) NOT NULL,
    state          VARCHAR(2)  NOT NULL,
    zip_code       VARCHAR(10) NOT NULL
);

CREATE TABLE client_associations (
    association_id       INTEGER AUTO_INCREMENT PRIMARY KEY,
    associated_client_id INTEGER NOT NULL,
    associated_person_id INTEGER NOT NULL,

    FOREIGN KEY (associated_client_id) REFERENCES client (client_id),
    FOREIGN KEY (associated_person_id) REFERENCES person (person_id)
);
