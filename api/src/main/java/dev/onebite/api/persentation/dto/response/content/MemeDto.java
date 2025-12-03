package dev.onebite.api.persentation.dto.response.content;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.onebite.api.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "image", "description", "tags", "createdAt"})
public class MemeDto extends ContentDto {

    private String image;
    private String description;

    public static MemeDto from(Content content) {
        MemeDto dto = new MemeDto(
                content.getImageUrl(),
                content.getDescription()
        );
        dto.id = content.getId();
        dto.type = "meme";
        dto.title = content.getTitle();
        dto.tags = content.getTags();
        dto.createdAt = content.getCreatedAt();
        return dto;
    }
}
