package abstraction;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> findById(Long id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);
}