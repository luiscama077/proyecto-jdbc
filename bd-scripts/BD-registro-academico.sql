-- =====================================
-- BASE DE DATOS: REGISTRO ACADÉMICO
-- =====================================

CREATE DATABASE IF NOT EXISTS registro_academico
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE registro_academico;

-- ==========================
-- TABLA DE ROLES
-- ==========================
CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE -- Ej: ADMIN, DOCENTE, ALUMNO
);

-- ==========================
-- TABLA DE USUARIOS
-- ==========================
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    id_rol INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- ==========================
-- TABLA DE ALUMNOS
-- ==========================
CREATE TABLE alumno (
    id_alumno INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    correo VARCHAR(100),
    id_usuario INT UNIQUE, -- Relación con usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ==========================
-- TABLA DE DOCENTES
-- ==========================
CREATE TABLE docente (
    id_docente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    correo VARCHAR(100),
    id_usuario INT UNIQUE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ==========================
-- TABLA DE CURSOS
-- ==========================
CREATE TABLE curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL,
    id_docente INT,
    FOREIGN KEY (id_docente) REFERENCES docente(id_docente)
);

-- ==========================
-- TABLA DE PERIODO ACADÉMICO
-- ==========================
CREATE TABLE periodo_academico (
    id_periodo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE -- Ej: 2025-I, 2025-II
);

-- ==========================
-- TABLA DE MATRÍCULAS
-- ==========================
CREATE TABLE matricula (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    id_curso INT NOT NULL,
    id_periodo INT NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (id_alumno) REFERENCES alumno(id_alumno),
    FOREIGN KEY (id_curso) REFERENCES curso(id_curso),
    FOREIGN KEY (id_periodo) REFERENCES periodo_academico(id_periodo),
    UNIQUE (id_alumno, id_curso, id_periodo) -- evita duplicados
);

-- ==========================
-- TABLA DE EVALUACIONES
-- ==========================
CREATE TABLE evaluacion (
    id_evaluacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE -- Ej: T1, T2, EF, Sustitutorio
);

-- ==========================
-- TABLA DE NOTAS
-- ==========================
CREATE TABLE nota (
    id_nota INT AUTO_INCREMENT PRIMARY KEY,
    id_matricula INT NOT NULL,
    id_evaluacion INT NOT NULL,
    nota DECIMAL(5,2) CHECK (nota >= 0 AND nota <= 20),
    FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula),
    FOREIGN KEY (id_evaluacion) REFERENCES evaluacion(id_evaluacion),
    UNIQUE (id_matricula, id_evaluacion) -- evita duplicados
);


-- ----------------------------------------------------------



-- ======================================================
-- SCRIPT DE INSERCIÓN DE DATOS PARA REGISTRO ACADÉMICO
-- ======================================================

-- Usar la base de datos correcta
USE registro_academico;

-- Roles del sistema
INSERT INTO rol (id_rol, nombre) VALUES (1, 'ADMIN'), (2, 'DOCENTE'), (3, 'ALUMNO');

-- Periodos Académicos
INSERT INTO periodo_academico (id_periodo, nombre) VALUES (1, '2025-I'), (2, '2025-II');

-- Tipos de Evaluaciones
INSERT INTO evaluacion (id_evaluacion, nombre) VALUES (1, 'T1'), (2, 'T2'), (3, 'Examen Parcial'), (4, 'Examen Final');

-- -----------------------------------------------------------------
-- 3. CREACIÓN DE USUARIOS Y SUS ROLES ASOCIADOS
-- Contraseña: [username]123
-- -----------------------------------------------------------------

-- Usuario Administrador
INSERT INTO usuario (username, password, id_rol) VALUES ('admin', 'admin123', 1);

-- Usuarios para Docentes
INSERT INTO usuario (username, password, id_rol) VALUES
('laura.campos', 'laura123', 2),
('javier.mendoza', 'javier123', 2),
('patricia.soto', 'patricia123', 2),
('ricardo.nunez', 'ricardo123', 2),
('monica.flores', 'monica123', 2);

-- Usuarios para Alumnos
INSERT INTO usuario (username, password, id_rol) VALUES
('sofia.perez', 'sofia123', 3),
('diego.rojas', 'diego123', 3),
('valentina.cruz', 'valentina123', 3),
('mateo.castillo', 'mateo123', 3),
('camila.diaz', 'camila123', 3),
('lucas.morales', 'lucas123', 3),
('isabella.rios', 'isabella123', 3),
('santiago.guzman', 'santiago123', 3);

-- -----------------------------------------------------------------
-- 4. CREACIÓN DE DOCENTES (ligados a sus usuarios)
-- -----------------------------------------------------------------
INSERT INTO docente (nombres, apellidos, dni, correo, id_usuario) VALUES
('Laura', 'Campos', '87654321', 'laura.campos@gmail.com', 2),
('Javier', 'Mendoza', '12348765', 'javier.mendoza@gmail.com', 3),
('Patricia', 'Soto', '23459876', 'patricia.soto@gmail.com', 4),
('Ricardo', 'Nuñez', '34561234', 'ricardo.nunez@gmail.com', 5),
('Mónica', 'Flores', '45672345', 'monica.flores@gmail.com', 6);

-- -----------------------------------------------------------------
-- 5. CREACIÓN DE ALUMNOS (ligados a sus usuarios)
-- -----------------------------------------------------------------
INSERT INTO alumno (nombres, apellidos, dni, correo, id_usuario) VALUES
('Sofía', 'Pérez', '45678901', 'sofia.perez@gmail.com', 7),
('Diego', 'Rojas', '56789012', 'diego.rojas@gmail.com', 8),
('Valentina', 'Cruz', '67890123', 'valentina.cruz@gmail.com', 9),
('Mateo', 'Castillo', '78901234', 'mateo.castillo@gmail.com', 10),
('Camila', 'Díaz', '89012345', 'camila.diaz@gmail.com', 11),
('Lucas', 'Morales', '90123456', 'lucas.morales@gmail.com', 12),
('Isabella', 'Ríos', '11234567', 'isabella.rios@gmail.com', 13),
('Santiago', 'Guzmán', '22345678', 'santiago.guzman@gmail.com', 14);

-- -----------------------------------------------------------------
-- 6. CREACIÓN DE CURSOS (y asignación de docentes)
-- -----------------------------------------------------------------
INSERT INTO curso (nombre, creditos, id_docente) VALUES
('Desarrollo Web Frontend', 4, 1),        -- Laura Campos
('Algoritmos y Complejidad', 4, 1),       -- Laura Campos
('Sistemas Operativos', 3, 2),            -- Javier Mendoza
('Redes y Comunicaciones', 3, 2),         -- Javier Mendoza
('Inteligencia Artificial', 4, 3),        -- Patricia Soto
('Machine Learning', 4, 3),               -- Patricia Soto
('Gestión de Proyectos de TI', 3, 4),     -- Ricardo Nuñez
('Calidad y Pruebas de Software', 3, 4),  -- Ricardo Nuñez
('Diseño de Experiencia de Usuario (UX)', 3, 5), -- Mónica Flores
('Arquitectura de Software', 4, 5);       -- Mónica Flores

-- -----------------------------------------------------------------
-- 7. MATRÍCULAS DE ALUMNOS EN CURSOS (Periodo 2025-II, id=2)
-- -----------------------------------------------------------------
INSERT INTO matricula (id_alumno, id_curso, id_periodo, fecha) VALUES
-- Sofía Pérez (id=1)
(1, 1, 2, '2025-08-10'), (1, 3, 2, '2025-08-10'), (1, 9, 2, '2025-08-10'),
-- Diego Rojas (id=2)
(2, 2, 2, '2025-08-11'), (2, 4, 2, '2025-08-11'), (2, 10, 2, '2025-08-11'),
-- Valentina Cruz (id=3)
(3, 1, 2, '2025-08-12'), (3, 5, 2, '2025-08-12'),
-- Mateo Castillo (id=4)
(4, 2, 2, '2025-08-12'), (4, 6, 2, '2025-08-12'),
-- Camila Díaz (id=5)
(5, 7, 2, '2025-08-13'), (5, 8, 2, '2025-08-13'),
-- Lucas Morales (id=6)
(6, 1, 2, '2025-08-13'), (6, 10, 2, '2025-08-13'),
-- Isabella Ríos (id=7)
(7, 5, 2, '2025-08-14'), (7, 9, 2, '2025-08-14'),
-- Santiago Guzmán (id=8)
(8, 3, 2, '2025-08-14'), (8, 7, 2, '2025-08-14');

-- -----------------------------------------------------------------
-- 8. REGISTRO DE NOTAS DE EJEMPLO
-- -----------------------------------------------------------------
INSERT INTO nota (id_matricula, id_evaluacion, nota) VALUES
-- Notas de Sofía Pérez
(1, 1, 16.00), (1, 2, 18.50), -- Frontend
(2, 1, 14.00),                -- Sistemas Operativos
-- Notas de Diego Rojas
(4, 1, 15.00), (4, 2, 13.50), -- Algoritmos
(5, 1, 17.00),                -- Redes
-- Notas de Valentina Cruz
(7, 1, 19.00), (7, 2, 20.00), -- Frontend
(8, 1, 18.00), (8, 2, 17.00), (8, 3, 16.00), -- IA
-- Notas de Lucas Morales
(12, 1, 12.50), (12, 2, 14.00), -- Frontend
(13, 1, 11.00);                 -- Arquitectura

