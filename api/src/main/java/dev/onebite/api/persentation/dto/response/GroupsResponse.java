package dev.onebite.api.persentation.dto.response;

import java.util.List;

public record GroupsResponse(
        String groupLabel,
        String groupKey,
        String icon,
        List<CategoriesResponse> categories
) {
}
