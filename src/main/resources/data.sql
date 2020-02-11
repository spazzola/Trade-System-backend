INSERT INTO buyers (name) VALUES
  ('Babuszka'),
  ('Czesyk'),
  ('Jarosik');

INSERT INTO suppliers (name) VALUES
  ('Malek');


INSERT INTO product (product_type) VALUES
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


INSERT INTO orders (quantity, buyer_fk, product_fk, supplier_fk) VALUES
    (10, 1, 5, 1),
    (15, 1, 12, 1),
    (50, 1, 12, 1);


INSERT INTO prices (product_fk, price, buyer_fk, supplier_fk) VALUES
    (1, 300, 1, null),
    (2, 300, 1, null),
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
    (1, 290, null, 1),
    (2, 290, null, 1),
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


INSERT INTO invoices (amount_to_use, invoice_number, is_paid, is_used, value, buyer_fk, supplier_fk) VALUES
    (10000, '100', true, false, 10000, 1, null),
    (10000, '101', true, false, 10000, 1, null),
    (15000, '113', true, false, 100000, null, 1);
