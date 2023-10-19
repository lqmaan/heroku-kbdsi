package icstar.kbdsi.apps.controllers;

import icstar.kbdsi.apps.dto.DeleteDto;
import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.models.Category;
import icstar.kbdsi.apps.models.User;
import icstar.kbdsi.apps.repository.CategoryRepository;
import icstar.kbdsi.apps.services.CategoryService;
import icstar.kbdsi.apps.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200", "https://kbdsi-icstar-fe.vercel.app/", "https://kbdsi-icstar-g0cg82eie-lh007lucky-gmailcom.vercel.app/" })

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepo;
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        super();
        this.categoryService = categoryService;
    }


    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategory (@RequestParam(required = false) String name){
        try{
            List<Category> categories = new ArrayList<>();

            if(name == null)
                categories.addAll(categoryService.getAllCategory());
//            else
//                categoryRepository.findAllByName(name, PageRequest.of(0, 10, Sort.by("name").ascending()));
            if(categories.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        try{
            Category newCategory =  new Category(category.getCategoryName(), category.getCreatedBy());
            categoryRepo.save(newCategory);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(required = true) Long id, @RequestBody DeleteDto deleteDto) {
        try {
            Optional<Category> optCategory = categoryRepo.findById(id);
            if(optCategory.isPresent()) {
                Category oldCategory = optCategory.get();
                oldCategory.setDeleted(true);
                oldCategory.setUpdatedBy(deleteDto.getUpdatedBy());
                return new ResponseEntity<>("Category has been deleted",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Budget doesn't exist",HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<Category> editCategory(@PathVariable(required = true) Long id, @RequestBody Category newCategory) {
        Optional<Category> optCategory = categoryRepo.findById(id);
        if(optCategory.isPresent()) {
            Category oldCategory = optCategory.get();
            oldCategory.setCategoryName(newCategory.getCategoryName());
            oldCategory.setUpdatedBy(newCategory.getUpdatedBy());
            return new ResponseEntity<>(categoryRepo.save(oldCategory),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
