package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.Category;
import icstar.kbdsi.apps.models.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> getCategoryById(Long id);
    List<Category> getAllCategory();

}
