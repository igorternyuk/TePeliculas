package tepeliculas.dto;

/**
 *
 * @author igor
 */
public class Country extends SimpleDTO{

    public Country() {
    }

    public Country(String name) {
        super(name);
    }

    public Country(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
