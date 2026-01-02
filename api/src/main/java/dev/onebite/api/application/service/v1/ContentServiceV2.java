package dev.onebite.api.application.service.v1;

import dev.onebite.api.domain.Content;
import dev.onebite.api.infra.repository.ContentRepository;
import dev.onebite.api.persentation.dto.response.ContentCursorResponse;
import dev.onebite.api.persentation.dto.response.content.ContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceV2 {

    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    public ContentCursorResponse getRandomizedContents(
            String seed,
            Long lastSeenId,
            String categories,
            int limit) {

        List<String> categoryList = parseCategories(categories);

        // lastSeenId의 해시값 계산
        String lastHash = null;
        if (lastSeenId != null) {
            lastHash = contentRepository.calculateHash(lastSeenId, seed);
        }

        // 데이터 조회 (단일 쿼리)
        List<Content> contents = categoryList.isEmpty()
                ? contentRepository.findRandomOrderedContentsAll(
                seed, lastHash, lastSeenId, limit + 1)
                : contentRepository.findRandomOrderedContentsByCategories(
                seed, lastHash, lastSeenId, categoryList, limit + 1);

        if (contents.isEmpty()) {
            return buildEmptyResponse();
        }

        boolean hasNext = contents.size() > limit;
        List<Content> targetContents = hasNext
                ? contents.subList(0, limit)
                : contents;

        List<ContentDto> contentDtos = targetContents.stream()
                .map(ContentDto::from)
                .toList();

        return ContentCursorResponse.builder()
                .content(contentDtos)
                .lastSeenId(hasNext ? targetContents.getLast().getId() : null)
                .hasNext(hasNext)
                .build();
    }

    private List<String> parseCategories(String categories) {
        if (categories == null || categories.isBlank() || "all".equalsIgnoreCase(categories)) {
            return Collections.emptyList();
        }
        return Arrays.stream(categories.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private ContentCursorResponse buildEmptyResponse() {
        return ContentCursorResponse.builder()
                .content(List.of())
                .lastSeenId(null)
                .hasNext(false)
                .build();
    }
}
