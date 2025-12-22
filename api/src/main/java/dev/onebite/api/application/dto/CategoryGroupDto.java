package dev.onebite.api.application.dto;

public record CategoryGroupDto(
        String groupLabel,
        String groupCode,
        String groupIconUrl,
        String categoryLabel,
        String categoryCode,
        String categoryIconUrl,
        Long contentCount
) {
    public CategoryGroupDto {
        contentCount = contentCount != null ? contentCount : 0L;
    }
}