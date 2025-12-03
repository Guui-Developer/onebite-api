package dev.onebite.api.application.service;

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
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    public ContentCursorResponse getRandomizedContents(
            String seed,
            Long lastCursorId,
            String categories,
            int limit) {

        int fetchSize = limit + 1;

        List<String> categoryList = parseCategories(categories);
        boolean isAllCategories = categoryList.isEmpty();

        List<Long> contentIds;
        PageRequest pageRequest = PageRequest.of(0, fetchSize);

        if (lastCursorId == null) {
            if (isAllCategories) {
                contentIds = contentRepository.findFirstPageIds(pageRequest);
            } else {
                contentIds = contentRepository.findFirstPageIdsByCategories(categoryList, pageRequest);
            }
        } else {
            if (isAllCategories) {
                contentIds = contentRepository.findNextPageIds(lastCursorId, pageRequest);
            } else {
                contentIds = contentRepository.findNextPageIdsByCategories(lastCursorId, categoryList, pageRequest);
            }
        }

        if (contentIds.isEmpty()) {
            return ContentCursorResponse.builder()
                    .content(List.of())
                    .lastSeenId(null)
                    .hasNext(false)
                    .build();
        }

        boolean hasNext = contentIds.size() > limit;

        List<Long> targetIds = hasNext
                ? contentIds.subList(0, limit)
                : contentIds;

        Long nextCursorId = targetIds.get(targetIds.size() - 1);

        List<Content> contents = contentRepository.findByIdsWithCategories(targetIds);

        Collections.shuffle(contents, new Random(seed.hashCode()));

        List<ContentDto> contentDtos = contents.stream()
                .map(ContentDto::from)
                .toList();

        return ContentCursorResponse.builder()
                .content(contentDtos)
                .lastSeenId(hasNext ? nextCursorId : null)
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
}
