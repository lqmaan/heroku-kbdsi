package icstar.kbdsi.apps.repository;

import icstar.kbdsi.apps.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeleted(boolean isDeleted);

    Category findByCategoryName(String name);
}
