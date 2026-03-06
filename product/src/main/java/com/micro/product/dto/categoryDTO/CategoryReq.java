package com.micro.product.dto.categoryDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryReq {
    @NotBlank
    private String name;
    private String description;
}
