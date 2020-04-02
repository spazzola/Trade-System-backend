INSERT INTO buyers (name) VALUES
  ('Babuszka'),
  ('Czesyk');

INSERT INTO suppliers (name) VALUES
  ('Malek');


INSERT INTO products (product) VALUES
    ('M_12_FRESH'),
    ('M_12_DRY'),
    ('M_6_FRESH'),
    ('M_6_DRY'),
    ('M_5_FRESH'),
    ('M_5_DRY'),
    ('M_4_FRESH'),
    ('M_4_DRY'),
    ('M_3_FRESH'),
    ('M_3_DRY'),
    ('M_2_5_FRESH'),
    ('M_2_5_DRY');

INSERT INTO prices (product_fk, price, buyer_fk, supplier_fk) VALUES
    (1, 538422.59, 1, null),
    (2, 1000, 1, null),
    (3, 280, 1, null),
    (4, 280, 1, null),
    (5, 260, 1, null),
    (6, 260, 1, null),
    (7, 240, 1, null),
    (8, 240, 1, null),
    (9, 220, 1, null),
    (10, 220, 1, null),
    (11, 200, 1, null),
    (12, 200, 1, null),
        (1, 7000, 2, null),
        (2, 4000, 2, null),
        (3, 280, 2, null),
        (4, 280, 2, null),
        (5, 260, 2, null),
        (6, 260, 2, null),
        (7, 240, 2, null),
        (8, 240, 2, null),
        (9, 220, 2, null),
        (10, 220, 2, null),
        (11, 200, 2, null),
        (12, 200, 2, null),
    (1, 455826.28, null, 1),
    (2, 900, null, 1),
    (3, 270, null, 1),
    (4, 270, null, 1),
    (5, 250, null, 1),
    (6, 250, null, 1),
    (7, 230, null, 1),
    (8, 230, null, 1),
    (9, 210, null, 1),
    (10, 210, null, 1),
    (11, 190, null, 1),
    (12, 190, null, 1);


