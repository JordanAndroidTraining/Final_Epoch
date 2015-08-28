package com.yahoo.shopping.spotplace.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jamesyan on 8/27/15.
 */
public interface SpotPlaceRepository extends CrudRepository<SpotPlace, Long> {
    List<SpotPlace> findByType(SpotPlaceType type);
}
