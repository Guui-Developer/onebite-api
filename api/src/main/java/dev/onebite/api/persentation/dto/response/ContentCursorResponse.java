package dev.onebite.api.persentation.dto.response;

import dev.onebite.api.persentation.dto.response.content.ContentDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContentCursorResponse {
    private List<ContentDto> content;
    private Long lastSeenId;
    private boolean hasNext;
}
