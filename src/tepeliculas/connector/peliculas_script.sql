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
INSERT INTO countries(name) VALUES('Portugal');

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
INSERT INTO directors VALUES(default, 'Manoel Cândido Pinto de Oliveira', 'Male', '1908-12-11', 9);
INSERT INTO directors VALUES(default, 'Oleg Pogodin', 'Male', '1965-07-03', 2);
INSERT INTO directors VALUES(default, 'Dmitriy Konstantinov', 'Male', '1968-02-25', 2);
INSERT INTO directors VALUES(default, 'Alfredo B. Crevenna', 'Male', '1914-04-22', 5);

CREATE TABLE movies(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    description TEXT,
    fk_classification INT,
    fk_genre INT,
    duration INT,
    release_date DATE,
    fk_director INT,
    picture LONGBLOB,
    rating INT,
    FOREIGN KEY(fk_classification) REFERENCES classifications(id),
    FOREIGN KEY(fk_genre) REFERENCES genres(id),
    FOREIGN KEY(fk_director) REFERENCES directors(id)
);

--drop database bd_peliculas;