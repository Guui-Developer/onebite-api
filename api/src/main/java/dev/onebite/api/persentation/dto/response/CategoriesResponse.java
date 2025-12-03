package dev.onebite.api.persentation.dto.response;

public record CategoriesResponse(
        String label,
        String key,
        String icon,
        int count
) {
}
