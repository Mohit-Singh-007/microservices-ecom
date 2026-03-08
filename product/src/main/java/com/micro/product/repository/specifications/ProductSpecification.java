package com.micro.product.repository.specifications;

import com.micro.product.models.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> withFilters(
            Boolean isActive, String search, Long categoryId, BigDecimal minPrice,BigDecimal maxPrice
            ){
        return Specification
                .where(hasActiveStatus(isActive))
                .and(hasKeyWord(search))
                .and(hasCategory(categoryId))
                .and(hasPriceRange(minPrice,maxPrice))
                ;
    }

    private static Specification<Product> hasActiveStatus(Boolean isActive){
        return ((root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"),isActive)
                );
    }

    private static Specification<Product> hasCategory(Long categoryId){
        return ((root, query, cb) ->
                categoryId==null ? null : cb.equal(root.get("category").get("categoryId"),categoryId)
                );
    }

    private static Specification<Product> hasPriceRange(BigDecimal min , BigDecimal max){
        return ((root, query, cb) -> {
            if (min==null && max==null) return null;
            if(min==null) return cb.lessThanOrEqualTo(root.get("price"),max);
            if(max == null) return cb.greaterThanOrEqualTo(root.get("price"),min);

            return cb.between(root.get("price"),min,max);
        });
    }

    private static Specification<Product> hasKeyWord(String search){
        return ((root, query, cb) ->{
         if(search == null || search.isBlank()) return null;

         String pattern = "%" + search.trim().toLowerCase() + "%";

         return cb.or(
                 cb.like(root.get("name"),pattern)
                 // can add more search criteria
         );
        });
    }

}
