package com.travelease.repositories;

import com.travelease.models.Route;
import com.travelease.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Override
    @NonNull
    Optional<Route> findById(@NonNull Long id);

    // ✅ Find Route by source and destination cities
    Optional<Route> findBySourceCityAndDestinationCity(City sourceCity, City destinationCity);
}
