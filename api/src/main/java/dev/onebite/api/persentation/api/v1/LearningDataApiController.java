package dev.onebite.api.persentation.api.v1;

import dev.onebite.api.application.service.ContentService;
import dev.onebite.api.persentation.dto.response.ContentCursorResponse;
import dev.onebite.api.persentation.dto.request.ContentSearchRequest;
import dev.onebite.api.persentation.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class LearningDataApiController {

    private final ContentService contentService;

    @GetMapping("/content")
    public ApiResponse<ContentCursorResponse> getAllContent(@Valid ContentSearchRequest request) {
        return ApiResponse.success(contentService.getRandomizedContents(request.seed(), request.lastSeenId(), request.categories(), request.limit()));
    }
}
