package com.travelease.repositories;

import com.travelease.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findBySourceCityIdAndDestinationCityId(Long sourceId, Long destinationId);
    Optional<Route> findById(Long id);
}