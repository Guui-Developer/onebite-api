package dev.onebite.api.persentation.dto.response.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.onebite.api.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "code", "language", "description", "tags", "createdAt"})
public class CodeTipDto extends ContentDto {
    private String code;
    private String language;
    private String description;

    public static CodeTipDto from(Content content) {
        CodeTipDto dto = new CodeTipDto(
                content.getCode(),
                content.getCode(),
                content.getDescription()
        );
        dto.id = content.getId();
        dto.type = "code_tip";
        dto.title = content.getTitle();
        dto.tags = content.getTags();
        dto.createdAt = content.getCreatedAt();
        return dto;
    }
}
