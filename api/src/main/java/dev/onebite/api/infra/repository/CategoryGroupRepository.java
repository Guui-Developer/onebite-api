package dev.onebite.api.infra.repository;

import dev.onebite.api.domain.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    @Query("SELECT DISTINCT cg FROM CategoryGroup cg JOIN FETCH cg.categories")
    List<CategoryGroup> findAllWithCategoriesFetchJoin();
}
