package dev.onebite.api.persentation.dto.response;

import java.util.List;

public record CategoryListResponse(
        List<GroupsResponse> groups,
        long totalCategories,
        long totalContent) {
}
