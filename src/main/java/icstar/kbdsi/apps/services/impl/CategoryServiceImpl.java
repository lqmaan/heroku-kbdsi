package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.models.Category;
import icstar.kbdsi.apps.repository.CategoryRepository;
import icstar.kbdsi.apps.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        super();
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findByIsDeleted(false);
    }
}
