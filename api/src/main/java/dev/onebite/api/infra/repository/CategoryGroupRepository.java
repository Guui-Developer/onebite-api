package dev.onebite.api.infra.repository;

import dev.onebite.api.application.dto.CategoryGroupDto;
import dev.onebite.api.domain.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    @Query("SELECT new dev.onebite.api.application.dto.CategoryGroupDto(" +
            "cg.label, cg.code, cg.iconUrl, " +
            "c.label, c.code, c.iconUrl, " +
            "COUNT(cc.id)) " +
            "FROM CategoryGroup cg " +
            "LEFT JOIN cg.categories c " +
            "LEFT JOIN c.categoryContents cc " +
            "GROUP BY cg.id, cg.label, cg.code, cg.iconUrl, cg.displayOrder, " +
            "c.id, c.label, c.code, c.iconUrl, c.displayOrder " +
            "ORDER BY cg.displayOrder ASC, c.displayOrder ASC")
    List<CategoryGroupDto> findAllCategoriesWithContentCount();
}
