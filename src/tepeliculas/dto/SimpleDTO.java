package tepeliculas.dto;

import java.util.Objects;

/**
 *
 * @author igor
 */
public class SimpleDTO {
    protected int id;
    protected String name;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SimpleDTO other = (SimpleDTO) obj;
        return this.id == other.id && Objects.equals(this.name, other.name);
    }
}
