DROP DATABASE IF EXISTS carnage;
CREATE DATABASE IF NOT EXISTS carnage ;

USE carnage;

-- Tabla de usuarios (admins y clientes)
CREATE TABLE IF NOT EXISTS users (
  id               INT             PRIMARY KEY AUTO_INCREMENT,
  user_name        VARCHAR(100)    NOT NULL,
  user_email       VARCHAR(200)    NOT NULL UNIQUE,
  user_password    VARCHAR(200)    NOT NULL,
  address          VARCHAR(300)    NULL,
  phone            VARCHAR(20)     NULL,
  role             ENUM('ADMIN','CLIENT') NOT NULL
);

-- Tabla de productos
CREATE TABLE IF NOT EXISTS  product (
  id               INT             PRIMARY KEY AUTO_INCREMENT,
  name             VARCHAR(150)    NOT NULL,
  price            DECIMAL(10,2)   NOT NULL,
  qty_in_stock     INT             NOT NULL,
  category         ENUM('BEEF','PORK','CHICKEN') NOT NULL,
  expiration_date  DATE            NULL
);

-- Tabla de ventas
CREATE TABLE IF NOT EXISTS  sale (
  id               INT             PRIMARY KEY AUTO_INCREMENT,
  client_id        INT             NOT NULL,
  sale_date        DATE            NOT NULL,
  total_price      DECIMAL(12,2)   NOT NULL,
  payment_method   ENUM('CASH','CARD','PAYPAL') NOT NULL,
  FOREIGN KEY (client_id) REFERENCES users(id)
);

-- Detalle de cada línea de venta (cantidad y precio en el momento de la venta)
CREATE TABLE IF NOT EXISTS sale_item (
  sale_id          INT             NOT NULL,
  product_id       INT             NOT NULL,
  quantity         INT             NOT NULL,
  price_at_sale    DECIMAL(10,2)   NOT NULL,
  PRIMARY KEY (sale_id, product_id),
  FOREIGN KEY (sale_id) REFERENCES sale(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Órdenes de proveedor
CREATE TABLE IF NOT EXISTS  supplier_order (
  id               INT             PRIMARY KEY AUTO_INCREMENT,
  order_date       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status           ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') NOT NULL
);

-- Detalle de cada línea de orden de proveedor
CREATE TABLE IF NOT EXISTS  supplier_order_item (
  id               INT             PRIMARY KEY AUTO_INCREMENT,
  order_id         INT             NOT NULL,
  product_id       INT             NOT NULL,
  quantity         INT             NOT NULL,
  FOREIGN KEY (order_id) REFERENCES supplier_order(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Facturas
CREATE TABLE IF NOT EXISTS  invoice (
  id               INT             PRIMARY KEY AUTO_INCREMENT,
  sale_id          INT             NOT NULL,
  invoice_date     DATE            NOT NULL,
  invoice_pdf      BLOB            NULL,
  FOREIGN KEY (sale_id) REFERENCES sale(id),
  UNIQUE (sale_id)
);

-- 1. Insertar el admin
INSERT INTO users (user_name, user_email, user_password, role)
VALUES 
  ('Miguel Admin', 'migue.626@gmail.com', 'milagrosa626', 'ADMIN');

-- 2. Insertar un cliente de ejemplo
INSERT INTO users (user_name, user_email, user_password, address, phone, role)
VALUES 
  ('Usuario Ejemplo', 'migueEjemplo01', '1234', 'Calle Falsa 123', '600123456', 'CLIENT');

-- Asumimos que el admin obtuvo id = 1 y el cliente id = 2:

-- 3. Crear 5 pedidos (ventas) para el cliente id = 2
INSERT INTO sale (client_id, sale_date, total_price, payment_method) VALUES
  (2, '2025-05-01',  45.50, 'CARD'),
  (2, '2025-05-03',  72.20, 'CASH'),
  (2, '2025-05-05',  33.80, 'PAYPAL'),
  (2, '2025-05-10', 128.00, 'CARD'),
  (2, '2025-05-15',  56.75, 'CASH');

-- 4. Insertar 20 productos distintos
INSERT INTO product (name, price, qty_in_stock, category, expiration_date) VALUES
  ('Entrecot de vacuno',     12.99,  50, 'BEEF',   '2025-06-01'),
  ('Filete de ternera',      11.50,  40, 'BEEF',   '2025-05-28'),
  ('Carne picada de vaca',    8.75,  60, 'BEEF',   '2025-05-30'),
  ('Solomillo de vacuno',    19.99,  10, 'BEEF',   '2025-06-08'),
  ('Hígado de ternera',       6.30,  12, 'BEEF',   '2025-05-22'),
  ('Costillas de cerdo',      9.20,  30, 'PORK',   '2025-06-05'),
  ('Lomo de cerdo',          10.00,  25, 'PORK',   '2025-06-02'),
  ('Jamón cocido',            7.50,  80, 'PORK',   '2025-07-01'),
  ('Chuletas de cerdo',       9.80,  35, 'PORK',   '2025-06-10'),
  ('Codillo de cerdo',        8.40,  18, 'PORK',   '2025-06-12'),
  ('Carrillera de cerdo',    11.75,  14, 'PORK',   '2025-06-07'),
  ('Pechuga de pollo',        6.99,  70, 'CHICKEN','2025-05-25'),
  ('Muslos de pollo',         5.50,  65, 'CHICKEN','2025-05-27'),
  ('Alitas de pollo',         4.75,  90, 'CHICKEN','2025-05-26'),
  ('Pollo entero',            8.20,  20, 'CHICKEN','2025-05-29'),
  ('Albondigas de pollo',     7.20,  50, 'CHICKEN','2025-05-24'),
  ('Mollejas de pollo',       5.90,  22, 'CHICKEN','2025-05-23'),
  ('Codorniz entera',         9.50,  16, 'CHICKEN','2025-05-31'),
  ('Carne picada mixta',      9.99,  48, 'PORK',   '2025-06-03'),
  ('Solomillo de cerdo',     14.25,  20, 'PORK',   '2025-06-15');