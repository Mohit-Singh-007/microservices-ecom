package com.micro.product.repository.specifications;

import com.micro.product.models.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecifications {

    public static Specification<Category> withFilters(Boolean isActive,String search){
        return Specification.where(hasActiveStatus(isActive))
                .and(hasKeyWord(search));
    }

    private static Specification<Category> hasActiveStatus(Boolean isActive){
        // null -> no filter , true -> active , false -> inactive
        return ((root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"),isActive));
    }

    private static Specification<Category> hasKeyWord(String search){

        return ((root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            String pattern = "%" + search.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")),pattern)
            );
        });
    }
}
