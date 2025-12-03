package dev.onebite.api.persentation.api.v1;

import dev.onebite.api.application.service.CategoryService;
import dev.onebite.api.persentation.dto.response.ApiResponse;
import dev.onebite.api.persentation.dto.response.CategoryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ApiResponse<CategoryListResponse> getAllCategories() {
        return ApiResponse.success(categoryService.getAllCategories());
    }
}
