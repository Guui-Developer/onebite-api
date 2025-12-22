package dev.onebite.api.application.service;

import dev.onebite.api.application.dto.CategoryGroupDto;
import dev.onebite.api.domain.CategoryGroup;
import dev.onebite.api.infra.repository.CategoryGroupRepository;
import dev.onebite.api.infra.repository.ContentRepository;
import dev.onebite.api.persentation.dto.response.CategoriesResponse;
import dev.onebite.api.persentation.dto.response.CategoryListResponse;
import dev.onebite.api.persentation.dto.response.GroupsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryGroupRepository categoryGroupRepository;
    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    public CategoryListResponse getAllCategories() {

        Map<String, List<CategoryGroupDto>> groupedByCode = categoryGroupRepository.findAllCategoriesWithContentCount().stream()
                .collect(Collectors.groupingBy(
                        CategoryGroupDto::groupCode,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<GroupsResponse> groupsResponses = groupedByCode.values().stream()
                .map(categoryGroupDtos -> {
                    CategoryGroupDto first = categoryGroupDtos.getFirst();

                    List<CategoriesResponse> categories = categoryGroupDtos.stream()
                            .filter(dto -> dto.categoryCode() != null)
                            .map(dto -> new CategoriesResponse(
                                    dto.categoryLabel(),
                                    dto.categoryCode(),
                                    dto.categoryIconUrl(),
                                    dto.contentCount().intValue()
                            )).toList();

                    return new GroupsResponse(
                            first.groupLabel(),
                            first.groupCode(),
                            first.groupIconUrl(),
                            categories
                    );
                }).toList();

        long totalContent = contentRepository.count();

        long totalCategories = groupsResponses.stream()
                .mapToLong(group -> group.categories().size())
                .sum();


        return new CategoryListResponse(groupsResponses, totalCategories, totalContent);
    }

}
