package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.exception.UniqueConstraintViolationException;
import it.cgmconsulting.myblog.payload.response.CategoryVisibleResponse;
import it.cgmconsulting.myblog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j //  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CategoryService.class);
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UtilsService utilsService;

    public ResponseEntity<?> save(String categoryName){
        log.info("Category persistence start");
        existsByCategoryName(categoryName);
        Category cat = new Category(categoryName);
        categoryRepository.save(cat);
        log.info("Category persisted: "+cat.toString());
        return new ResponseEntity(cat, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllVisibleCategories(){
        log.info("Get all visible categories ordered by category name");
        List<CategoryVisibleResponse> list = categoryRepository.getAllVisibleCategories();
        utilsService.isEmptyCollection(list, "categories");
        return new ResponseEntity(list, HttpStatus.OK);
    }

    public ResponseEntity<?> findAll(){
        log.info("Get all categories");
        List<Category> list = categoryRepository.findAll();
        utilsService.isEmptyCollection(list, "categories");
        return new ResponseEntity(list, HttpStatus.OK);
    }

    public ResponseEntity<?> update(byte id, String newCategory){
        existsByCategoryName(newCategory);
        Category cat =  getCategoryById(id);
        cat.setCategoryName(newCategory);
        categoryRepository.save(cat);
        return new ResponseEntity("Category has been updated", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> switchVisibility(byte id){
        Category cat =  getCategoryById(id);
        cat.setVisible(!cat.isVisible());
        return new ResponseEntity(null, HttpStatus.OK);
    }

    protected void existsByCategoryName(String categoryName){
        if(categoryRepository.existsByCategoryName(categoryName))
            throw new UniqueConstraintViolationException("Category", "category name", categoryName);
    }

    protected Category getCategoryByIdAndVisibleTrue(byte categoryId){
        return categoryRepository.findByIdAndVisibleTrue(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    protected Category getCategoryById(byte categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

}
