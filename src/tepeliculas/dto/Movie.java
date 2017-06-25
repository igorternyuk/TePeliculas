package tepeliculas.dto;

/**
 *
 * @author igor
 */
public class Movie extends SimpleDTO {
    private String description;
   /*    id INT NOT NULL PRIMARY KEY auto_increment,
    nombre VARCHAR(200),
    descripcion VARCHAR(200),
    fk_clasificacion INT,
    fk_genero INT,
    duracion INT,
    fecha_estreno DATE,
    fk_director INT,
    picture LONGBLOB,
    puntuacion INT,
    FOREIGN KEY(fk_clasificacion) REFERENCES clasificacion(id),
    FOREIGN KEY(fk_genero) REFERENCES generos(id),
    FOREIGN KEY(fk_director) REFERENCES directores(id)*/ 
}
