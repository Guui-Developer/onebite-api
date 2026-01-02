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

    @Query(value = "SELECT c.id FROM Content c ORDER BY c.id DESC")
    List<Long> findFirstPageIds(Pageable pageable);

    @Query(value = "SELECT c.id FROM Content c WHERE c.id < :lastCursorId ORDER BY c.id DESC")
    List<Long> findNextPageIds(@Param("lastCursorId") Long lastCursorId, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Content c " +
            "LEFT JOIN FETCH c.categoryContents cc " +
            "LEFT JOIN FETCH cc.categoryId " +
            "WHERE c.id IN :ids " +
            "ORDER BY c.id DESC")
    List<Content> findByIdsWithCategories(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT c.id FROM Content c " +
            "JOIN c.categoryContents cc " +
            "JOIN cc.categoryId cat " +
            "WHERE cat.code IN :categories " +
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

    // 전체 카테고리
    @Query(value = """
        SELECT c.*
        FROM devonebite.content c
        WHERE (
            :lastHash IS NULL 
            OR MD5(CONCAT(CAST(c.id AS VARCHAR), :seed)) > :lastHash
            OR (
                MD5(CONCAT(CAST(c.id AS VARCHAR), :seed)) = :lastHash 
                AND c.id > :lastId
            )
        )
        ORDER BY MD5(CONCAT(CAST(c.id AS VARCHAR), :seed)), c.id
        LIMIT :limit
        """, nativeQuery = true)
    List<Content> findRandomOrderedContentsAll(
            @Param("seed") String seed,
            @Param("lastHash") String lastHash,
            @Param("lastId") Long lastId,
            @Param("limit") int limit
    );

    // 카테고리별 - DISTINCT 제거하고 서브쿼리 사용
    @Query(value = """
        SELECT c.*
        FROM devonebite.content c
        WHERE c.id IN (
            SELECT DISTINCT cc.content_id
            FROM devonebite.category_content cc
            INNER JOIN devonebite.categories cat ON cc.category_id = cat.id
            WHERE cat.code IN :categories
        )
        AND (
            :lastHash IS NULL 
            OR MD5(CONCAT(CAST(c.id AS VARCHAR), :seed)) > :lastHash
            OR (
                MD5(CONCAT(CAST(c.id AS VARCHAR), :seed)) = :lastHash 
                AND c.id > :lastId
            )
        )
        ORDER BY MD5(CONCAT(CAST(c.id AS VARCHAR), :seed)), c.id
        LIMIT :limit
        """, nativeQuery = true)
    List<Content> findRandomOrderedContentsByCategories(
            @Param("seed") String seed,
            @Param("lastHash") String lastHash,
            @Param("lastId") Long lastId,
            @Param("categories") List<String> categories,
            @Param("limit") int limit
    );

    // 해시값 계산
    @Query(value = """
        SELECT MD5(CONCAT(CAST(:contentId AS VARCHAR), :seed))
        """, nativeQuery = true)
    String calculateHash(
            @Param("contentId") Long contentId,
            @Param("seed") String seed
    );
}
