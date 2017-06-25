package tepeliculas.dto;

/**
 *
 * @author igor
 */
public class Genre extends SimpleDTO {

    public Genre() {
    }

    public Genre(String name) {
        super(name);
    }

    public Genre(int id, String name) {
        super(id, name);
    }
}
