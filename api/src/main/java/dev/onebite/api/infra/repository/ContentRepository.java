package dev.onebite.api.infra.repository;

import dev.onebite.api.domain.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // Step 1: ID만 조회 (페이지네이션 정확)
    @Query(value = "SELECT c.id FROM Content c ORDER BY c.id DESC")
    List<Long> findFirstPageIds(Pageable pageable);

    @Query(value = "SELECT c.id FROM Content c WHERE c.id < :lastCursorId ORDER BY c.id DESC")
    List<Long> findNextPageIds(@Param("lastCursorId") Long lastCursorId, Pageable pageable);

    // Step 2: ID로 전체 데이터 조회 (카테고리 포함)
    @Query("SELECT DISTINCT c FROM Content c " +
            "LEFT JOIN FETCH c.categoryContents cc " +
            "LEFT JOIN FETCH cc.categoryId " +
            "WHERE c.id IN :ids " +
            "ORDER BY c.id DESC")
    List<Content> findByIdsWithCategories(@Param("ids") List<Long> ids);

    // [추가] 카테고리 필터용 (Join 필요)
    // DISTINCT 필수: 하나의 컨텐츠가 'JS', 'React' 두 개 다 가질 경우 중복 ID 방지
    @Query("SELECT DISTINCT c.id FROM Content c " +
            "JOIN c.categoryContents cc " +
            "JOIN cc.categoryId cat " +  // 실제 카테고리 엔티티 조인 (필드명에 맞게 수정 필요)
            "WHERE cat.code IN :categories " + // 카테고리 이름으로 필터링
            "ORDER BY c.id DESC")
    List<Long> findFirstPageIdsByCategories(
            @Param("categories") List<String> categories,
            Pageable pageable);

    @Query("SELECT DISTINCT c.id FROM Content c " +
            "JOIN c.categoryContents cc " +
            "JOIN cc.categoryId cat " +
            "WHERE c.id < :lastCursorId " +
            "AND cat.code IN :categories " +
            "ORDER BY c.id DESC")
    List<Long> findNextPageIdsByCategories(
            @Param("lastCursorId") Long lastCursorId,
            @Param("categories") List<String> categories,
            Pageable pageable);
}
