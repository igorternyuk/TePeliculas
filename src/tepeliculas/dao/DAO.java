package tepeliculas.dao;

import java.util.List;
/**
 *
 * @author igor
 */
public interface DAO<T> {
    void insert(T register);
    void update(T register);
    void delete(int id);
    T read(int id);
    List<T> readAll();
}
