package dev.onebite.api.persentation.dto.response.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.onebite.api.domain.Content;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CodeTipDto.class, name = "code_tip"),
        @JsonSubTypes.Type(value = BugChallengeDto.class, name = "bug_challenge"),
        @JsonSubTypes.Type(value = CodeReviewDto.class, name = "code_review"),
        @JsonSubTypes.Type(value = MemeDto.class, name = "meme"),
        @JsonSubTypes.Type(value = InterviewDto.class, name = "interview")
})
@JsonPropertyOrder({"id", "type", "title", "tags", "createdAt"})
public abstract class ContentDto {
    protected Long id;
    protected String type;
    protected String title;
    protected List<String> tags;
    protected LocalDateTime createdAt;

    public static ContentDto from(Content content) {
        return switch (content.getType()) {
            case CODE_TIP -> CodeTipDto.from(content);
            case BUG_CHALLENGE -> BugChallengeDto.from(content);
            case CODE_REVIEW -> CodeReviewDto.from(content);
            case MEME -> MemeDto.from(content);
            case INTERVIEW -> InterviewDto.from(content);
        };
    }
}
