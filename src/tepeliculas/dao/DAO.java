package tepeliculas.dao;

import java.util.List;
/**
 *
 * @author igor
 */
public interface DAO<T> {
    boolean insert(T register);
    boolean update(T register);
    boolean delete(int id);
    T read(int id);
    List<T> readAll();
}
