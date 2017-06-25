/**
 * Author:  igor
 * Created: Jun 24, 2017
 */

--
CREATE DATABASE db_movies;

use db_movies;

CREATE TABLE countries(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200)
);

INSERT INTO countries(name) VALUES('Ukraine');
INSERT INTO countries(name) VALUES('Russia');
INSERT INTO countries(name) VALUES('USA');
INSERT INTO countries(name) VALUES('Spain');
INSERT INTO countries(name) VALUES('Mexico');
INSERT INTO countries(name) VALUES('Brazil');
INSERT INTO countries(name) VALUES('England');
INSERT INTO countries(name) VALUES('Germany');

CREATE TABLE classifications(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200)
);

INSERT INTO classifications(name) VALUES('Todos los espectadores');
INSERT INTO classifications(name) VALUES('Infantil');
INSERT INTO classifications(name) VALUES('12+');
INSERT INTO classifications(name) VALUES('16+');
INSERT INTO classifications(name) VALUES('21+');
INSERT INTO classifications(name) VALUES('Adulto');

CREATE TABLE genres(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200)
);

INSERT INTO genres VALUES(default, 'Comedia');
INSERT INTO genres VALUES(default, 'Tragedia');
INSERT INTO genres VALUES(default, 'Tragicomedia');
INSERT INTO genres VALUES(default, 'pieza (teatral)');
INSERT INTO genres VALUES(default, 'Accion');
INSERT INTO genres VALUES(default, 'Deporte');
INSERT INTO genres  VALUES(default, 'Melodrama');
INSERT INTO genres  VALUES(default, 'Viaje');
INSERT INTO genres  VALUES(default, 'Detective');
INSERT INTO genres  VALUES(default, 'Combatiente');
INSERT INTO genres  VALUES(default, 'Animales');
INSERT INTO genres  VALUES(default, 'Ficción');

CREATE TABLE directors(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    gender enum('Male', 'Female'),
    dob DATE,
    fk_country INT,
    FOREIGN KEY(fk_country) REFERENCES countries(id)
);

INSERT INTO directors VALUES(default, 'Eldar Ryazanov', 'Male', '1927-11-18', 2);

CREATE TABLE movies(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    description TEXT,
    fk_classification INT,
    fk_genre INT,
    duration INT,
    fecha_estreno DATE,
    fk_director INT,
    picture LONGBLOB,
    rating INT,
    FOREIGN KEY(fk_classification) REFERENCES classifications(id),
    FOREIGN KEY(fk_genre) REFERENCES genres(id),
    FOREIGN KEY(fk_director) REFERENCES directors(id)
);

--drop database bd_peliculas;