/**
 * Author:  igor
 * Created: Jun 24, 2017
 */

--
CREATE DATABASE bd_peliculas;

use bd_peliculas;
CREATE TABLE clasificacion(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(200)
);

INSERT INTO clasificacion(nombre) VALUES('Todos los espectadores');
INSERT INTO clasificacion(nombre) VALUES('Infantil');
INSERT INTO clasificacion(nombre) VALUES('12+');
INSERT INTO clasificacion(nombre) VALUES('16+');
INSERT INTO clasificacion(nombre) VALUES('21+');
INSERT INTO clasificacion(nombre) VALUES('Adulto');

CREATE TABLE generos(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(200)
);

INSERT INTO generos VALUES(default, 'Comedia');
INSERT INTO generos VALUES(default, 'Tragedia');
INSERT INTO generos VALUES(default, 'Tragicomedia');
INSERT INTO generos VALUES(default, 'pieza (teatral)');
INSERT INTO generos VALUES(default, 'Accion');
INSERT INTO generos VALUES(default, 'Deporte');
INSERT INTO generos  VALUES(default, 'Melodrama');
INSERT INTO generos  VALUES(default, 'Viaje');
INSERT INTO generos  VALUES(default, 'Detective');
INSERT INTO generos  VALUES(default, 'Combatiente');
INSERT INTO generos  VALUES(default, 'Animales');
INSERT INTO generos  VALUES(default, 'Ficción');

CREATE TABLE directores(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50)
);

INSERT INTO directores VALUES(default, 'Eldar Ryazanov');

CREATE TABLE peliculas(
    id INT NOT NULL PRIMARY KEY auto_increment,
    nombre VARCHAR(200),
    descripcion TEXT,
    fk_clasificacion INT,
    fk_genero INT,
    duracion INT,
    fecha_estreno DATE,
    fk_director INT,
    picture LONGBLOB,
    puntuacion INT,
    FOREIGN KEY(fk_clasificacion) REFERENCES clasificacion(id),
    FOREIGN KEY(fk_genero) REFERENCES generos(id),
    FOREIGN KEY(fk_director) REFERENCES directores(id)
);