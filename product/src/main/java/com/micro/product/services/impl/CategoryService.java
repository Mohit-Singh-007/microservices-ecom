package com.micro.product.services.impl;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.exceptions.CategoryAlreadyExists;
import com.micro.product.exceptions.CategoryNotFoundException;
import com.micro.product.models.Category;
import com.micro.product.repository.CategoryRepo;
import com.micro.product.repository.specifications.CategorySpecification;
import com.micro.product.services.CategoryServiceInterface;
import com.micro.product.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {


    private final CategoryRepo categoryRepo;

    @Override
    public CategoryRes createCategory(CategoryReq req) {

        String name = req.getName().trim().toLowerCase();
        categoryRepo.findByName(name)
                .ifPresent((c)->{
                    throw new CategoryAlreadyExists("Category already exists...");
                });

        Category c = new Category();
        c.setName(name);
        c.setDescription(req.getDescription());

        Category saved = categoryRepo.save(c);
        return mapToCategoryRes(saved);
    }

    @Override
    public PaginatedResponse<CategoryRes> getAllCategories(Pageable pageable) {
        Page<Category> page = categoryRepo.findAllByIsActiveTrue(pageable);

        List<CategoryRes> res = page.stream().map(this::mapToCategoryRes).toList();

        return new PaginatedResponse<>(res,page);

    }

    @Override
    public CategoryRes getCategoryById(Long id) {
        Category c = categoryRepo.findByCategoryIdAndIsActiveTrue(id)
                .orElseThrow(()-> new CategoryNotFoundException("Cannot find category with id: "+id));

        return mapToCategoryRes(c);
    }


    public PaginatedResponse<CategoryRes> getCategoriesForAdmin(
            Boolean isActive,
            String search,
            Pageable pageable
    ) {


        Specification<Category> spec = CategorySpecification.withFilters(isActive,search);

        Page<Category> c = categoryRepo.findAll(spec, pageable);

        List<CategoryRes> res = c.stream()
                .map(this::mapToCategoryRes)
                .toList();

        return new PaginatedResponse<>(res, c);
    }


    @Override
    public void toggleCategoryStatus(Long id) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Cannot find category with id: "+id));
        c.setActive(!c.isActive());
        categoryRepo.save(c);
    }

    CategoryRes mapToCategoryRes(Category c){
        CategoryRes res = new CategoryRes();
        res.setId(c.getCategoryId());
        res.setName(c.getName());
        res.setDescription(c.getDescription());
        res.setActive(c.isActive());
        res.setCreatedAt(c.getCreatedAt());
        return res;
    }
}
