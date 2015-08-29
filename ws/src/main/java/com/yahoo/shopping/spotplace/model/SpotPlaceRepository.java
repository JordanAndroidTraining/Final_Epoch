package com.yahoo.shopping.spotplace.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jamesyan on 8/27/15.
 */
public interface SpotPlaceRepository extends CrudRepository<SpotPlace, Long> {
    Page<SpotPlace> findByType(SpotPlaceType type, Pageable pageable);

    @Query("select sp from SpotPlace sp where (title like :keyword or address like :keyword or feature like :keyword)")
    Page<SpotPlace> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select sp from SpotPlace sp where (title like :keyword or address like :keyword or feature like :keyword) and type = :type")
    Page<SpotPlace> findByKeywordAndType(@Param("keyword") String keyword, @Param("type") SpotPlaceType type, Pageable pageable);
}
