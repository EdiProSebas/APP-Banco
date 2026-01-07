DROP DATABASE IF EXISTS banco_db;
CREATE DATABASE banco_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE banco_db;

DROP TABLE IF EXISTS movimientos;
DROP TABLE IF EXISTS cuentas;
DROP TABLE IF EXISTS clientes;

CREATE TABLE clientes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  cliente_id VARCHAR(30) UNIQUE NOT NULL,
  contrasena VARCHAR(100) NOT NULL,
  estado BOOLEAN NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  genero VARCHAR(20) NOT NULL,
  edad INT NOT NULL,
  identificacion VARCHAR(30) UNIQUE NOT NULL,
  direccion VARCHAR(200) NOT NULL,
  telefono VARCHAR(20) NOT NULL
);

CREATE TABLE cuentas (
  cuenta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  numero_cuenta VARCHAR(30) UNIQUE NOT NULL,
  tipo_cuenta ENUM('AHORROS','CORRIENTE') NOT NULL,
  saldo_inicial DOUBLE NOT NULL,
  estado BIT(1) NOT NULL,
  cliente_pk BIGINT NOT NULL,
  CONSTRAINT fk_cuenta_cliente_pk FOREIGN KEY (cliente_pk) REFERENCES clientes(id)
);

CREATE TABLE movimientos (
  movimiento_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  fecha DATETIME NOT NULL,
  tipo_movimiento ENUM('DEBITO','CREDITO') NOT NULL,
  valor DOUBLE NOT NULL,
  saldo DOUBLE NOT NULL,
  cuenta_id BIGINT NOT NULL,
  CONSTRAINT fk_movimiento_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas(cuenta_id)
);

-- Datos de ejemplo (si ya tienes, puedes comentar esta sección)
INSERT INTO clientes (cliente_id, contrasena, estado, nombre, genero, edad, identificacion, direccion, telefono)
VALUES
('CLI-0001','Pass1234',1,'Jose Lema','MASCULINO',30,'1712345678','Otavalo sn y principal','098254785'),
('CLI-0002','Pass5678',1,'Marianela Montalvo','FEMENINO',25,'1723456789','Amazonas y NNUU','097548965'),
('CLI-0003','Pass1245',1,'Juan Osorio','MASCULINO',28,'1734567890','13 junio y Equinoccial','098874587');

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_pk)
VALUES
('478758','AHORROS',2000,b'1',1),
('225487','CORRIENTE',100,b'1',2),
('495878','AHORROS',0,b'1',3),
('496825','AHORROS',540,b'1',2);

-- Movimientos: (saldo final calculado a mano para demo)
INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, cuenta_id)
VALUES
('2026-01-01 10:00:00','DEBITO',575,1425,1),
('2026-01-01 11:00:00','CREDITO',600,700,2),
('2026-01-01 12:00:00','CREDITO',150,150,3),
('2026-01-01 13:00:00','DEBITO',540,0,4);