package dev.onebite.api.persentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ContentSearchRequest(
        @Min(1) @Max(100)
        int limit,
        String categories,
        Long lastSeenId,
        String seed
) {
}
