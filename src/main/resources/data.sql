INSERT INTO users (email, login, password, role) VALUES
('fake@gg.pl', 'f', '$2a$10$6j6dBYrzlq.9L.gKp4sm6ur4qy75bNcUlRrgeD8OALeSItmlIZlGi', 'ADMIN');

INSERT INTO buyers(name) VALUES
('Babuszka');

INSERT INTO suppliers(name) VALUES
('LesyCR');

INSERT INTO products(product) VALUES
('12m');

INSERT INTO prices(price, buyer_fk, product_fk) VALUES
(100, 1, 1);

INSERT INTO prices(price, supplier_fk, product_fk) VALUES
(90, 1, 1);

INSERT INTO invoices(amount_to_use, value, date, is_paid, is_used, supplier_fk, is_created_to_order, to_equalize_negative_invoice) VALUES
(10000, 10000, '2020-11-28', true, false, 1, false, false);


