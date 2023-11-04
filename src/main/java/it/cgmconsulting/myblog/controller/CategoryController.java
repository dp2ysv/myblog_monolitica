package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.CategoryService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/") // localhost:8081/category/?categoryName=antipasti Method: POST
    public ResponseEntity<?> save(@RequestParam @NotBlank @Size(min = 4, max = 30) String categoryName){
        return categoryService.save(categoryName.trim()); // __antipasti____
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{categoryName}") // localhost:8081/category/antipasti Method: POST
    public ResponseEntity<?> save2(@PathVariable @NotBlank @Size(min = 4, max = 30) String categoryName){
        return categoryService.save(categoryName.trim());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add") // localhost:8081/category/add Method: POST
    public ResponseEntity<?> save3(@RequestBody @NotBlank @Size(min = 4, max = 30) String categoryName){
        return categoryService.save(categoryName.trim());
    }

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @GetMapping // localhost:8081/category
    public ResponseEntity<?> getAllVisibleCategories(){
        return categoryService.getAllVisibleCategories();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all") // localhost:8081/category/all
    public ResponseEntity<?> findAll(){
        return categoryService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}") // localhost:8081/category/5?newCategory=carni
    public ResponseEntity<?> update(@PathVariable @Min(1) byte id, @RequestParam @NotBlank @Size(min = 4, max = 30) String newCategory){
        return categoryService.update(id, newCategory.trim());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/switch-visibility/{id}") // localhost:8081/category/switch-visibility/3
    public ResponseEntity<?> switchVisibility(@PathVariable @Min(1) byte id){
        return categoryService.switchVisibility(id);
    }

}
