package dev.onebite.api.persentation.dto.response.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.onebite.api.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "language", "code", "answer", "tags", "createdAt"})
public class BugChallengeDto extends ContentDto {

    private String code;
    private String answer;
    private String language;

    public static BugChallengeDto from(Content content) {
        BugChallengeDto dto = new BugChallengeDto(
                content.getCode(),
                content.getAnswer(),
                content.getLanguage()
        );
        dto.id = content.getId();
        dto.type = "bug_challenge";
        dto.title = content.getTitle();
        dto.tags = content.getTags();
        dto.createdAt = content.getCreatedAt();
        return dto;
    }
}
