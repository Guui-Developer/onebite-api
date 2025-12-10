package dev.onebite.api.domain;

import dev.onebite.api.infra.enums.ContentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private ContentType type;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "before_code", columnDefinition = "TEXT")
    private String beforeCode;

    @Column(name = "after_code", columnDefinition = "TEXT")
    private String afterCode;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "question", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "views", columnDefinition = "int4 default 0")
    private Integer views;

    @Column(name = "bookmarks", columnDefinition = "int4 default 0")
    private Integer bookmarks;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "contentId", fetch = FetchType.LAZY)
    private List<CategoryContent> categoryContents = new ArrayList<>();

    public List<String> getTags() {
        return categoryContents.stream()
                .map(cc -> cc.getCategoryId().getCode())
                .collect(Collectors.toList());
    }
}
