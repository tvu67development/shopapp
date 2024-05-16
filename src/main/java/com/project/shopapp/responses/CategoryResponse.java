package com.project.shopapp.responses;

import com.project.shopapp.models.Category;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {

    private String message;

    private List<String> errors;

    private Category category;
}
