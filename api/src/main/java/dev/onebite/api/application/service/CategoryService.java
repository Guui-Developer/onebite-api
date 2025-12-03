package dev.onebite.api.application.service;

import dev.onebite.api.domain.CategoryGroup;
import dev.onebite.api.infra.repository.CategoryGroupRepository;
import dev.onebite.api.persentation.dto.response.CategoriesResponse;
import dev.onebite.api.persentation.dto.response.CategoryListResponse;
import dev.onebite.api.persentation.dto.response.GroupsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryGroupRepository categoryGroupRepository;

    @Transactional(readOnly = true)
    public CategoryListResponse getAllCategories() {
        List<CategoryGroup> groups = categoryGroupRepository.findAllWithCategoriesFetchJoin();

        List<GroupsResponse> groupsResponses = groups.stream()
                .map(group -> new GroupsResponse(
                        group.getLabel(),
                        group.getCode(),
                        group.getIconUrl(),
                        group.getCategories().stream()
                                .map(category -> new CategoriesResponse(
                                        category.getLabel(),
                                        category.getCode(),
                                        category.getIconUrl(),
                                        category.getCategoryContents().size()
                                )).toList()
                )).toList();


        long totalCategories = groups.stream()
                .mapToLong(group -> group.getCategories().size())
                .sum();

        long totalContent = groups.stream()
                .flatMap(group -> group.getCategories().stream())
                .mapToLong(category -> category.getCategoryContents().size())
                .sum();

        return new CategoryListResponse(groupsResponses, totalCategories, totalContent);
    }

}
