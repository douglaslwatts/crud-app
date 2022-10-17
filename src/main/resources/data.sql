INSERT INTO person (
    first_name,
    last_name,
    email_address,
    street_address,
    city,
    state,
    zip_code
) VALUES (
    'John',
    'Smith',
    'fake1@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801'
), (
    'Jane',
    'Smith',
    'fake2@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801'
);

INSERT INTO client (
    company_name,
    website,
    phone,
    street_address,
    city,
    state,
    zip_code
) VALUES (
    'Forest Software',
    'forestsoftware.com',
    '8283294657',
    '1297 Forest Ave.',
    'Asheville',
    'NC',
    '28803'
), (
    'Java Beans Coffee Shop',
    'javabeanscoffee.com',
    '(828) 491-3092',
    '897 Jittery Ln.',
    'Asheville',
    'NC',
    '28801'
);

INSERT INTO client_person_associations (
    client_id,
    person_id
) VALUES (
    1,
    2
), (
    2,
    1
);
