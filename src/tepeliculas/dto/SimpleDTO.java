package tepeliculas.dto;

/**
 *
 * @author igor
 */
public class SimpleDTO {
    int id;
    String name;

    public SimpleDTO() {
    }

    public SimpleDTO(String name) {
        this.name = name;
    }

    public SimpleDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
