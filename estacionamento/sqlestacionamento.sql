CREATE DATABASE Estacionamento;
USE Estacionamento;

CREATE TABLE Clientes (
    nome VARCHAR(75),
    id VARCHAR(20) PRIMARY KEY,
    telefone VARCHAR(20)
);

CREATE TABLE vagas (
	numero INT PRIMARY KEY NOT NULL UNIQUE,        
    placa_carro varchar(10),
    status ENUM('livre', 'ocupada') DEFAULT 'livre'
);

CREATE TABLE Carros (
    placa VARCHAR(10) NOT NULL PRIMARY KEY,
    modelo VARCHAR(50) NOT NULL,
    cor VARCHAR(20),
    id_dono VARCHAR(20),
    vaga INT,
    horario_entrada DATETIME NOT NULL,
    FOREIGN KEY (id_dono) REFERENCES Clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (vaga) REFERENCES vagas(numero) ON DELETE SET NULL
);

ALTER TABLE vagas
ADD CONSTRAINT fk_vaga_carro FOREIGN KEY (placa_carro) REFERENCES Carros(placa) ON DELETE SET NULL;

INSERT INTO vagas (numero, status) 
VALUES 
(1, 'livre'), (2, 'livre'), (3, 'livre'), (4, 'livre'), (5, 'livre'),
(6, 'livre'), (7, 'livre'), (8, 'livre'), (9, 'livre'), (10, 'livre'),
(11, 'livre'), (12, 'livre'), (13, 'livre'), (14, 'livre'), (15, 'livre'),
(16, 'livre'), (17, 'livre'), (18, 'livre'), (19, 'livre'), (20, 'livre'),
(21, 'livre'), (22, 'livre'), (23, 'livre'), (24, 'livre'), (25, 'livre'),
(26, 'livre'), (27, 'livre'), (28, 'livre'), (29, 'livre'), (30, 'livre'),
(31, 'livre'), (32, 'livre'), (33, 'livre'), (34, 'livre'), (35, 'livre'),
(36, 'livre'), (37, 'livre'), (38, 'livre'), (39, 'livre'), (40, 'livre'),
(41, 'livre'), (42, 'livre'), (43, 'livre'), (44, 'livre'), (45, 'livre'),
(46, 'livre'), (47, 'livre'), (48, 'livre'), (49, 'livre'), (50, 'livre');

-- INSERTS PARA TESTAR O VALOR 
INSERT INTO Clientes (nome, id, telefone) VALUES
('João Silva', '1', '11987654321'),
('Maria Oliveira', '2', '21987654321'),
('Carlos Pereira', '3', '31987654321');

INSERT INTO Carros (placa, modelo, cor, id_dono, vaga, horario_entrada) VALUES 
('ABC1234', 'Ford Ka', 'Preto', '1', 1, '2025-01-08 19:30:00'), 
('XYZ5678', 'Civic', 'Prata', '2', 2, '2025-01-08 02:00:00'),
('LMN9101', 'HB20', 'Vermelho', '3', 3, '2025-01-09 09:15:00'); 

