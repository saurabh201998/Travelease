package com.travelease.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "source_city_id")
    @NotNull(message = "Source city is required")
    private City sourceCity;
    
    @ManyToOne
    @JoinColumn(name = "destination_city_id")
    @NotNull(message = "Destination city is required")
    private City destinationCity;
    
    private Integer distanceInKm;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public City getSourceCity() { return sourceCity; }
    public void setSourceCity(City sourceCity) { this.sourceCity = sourceCity; }
    
    public City getDestinationCity() { return destinationCity; }
    public void setDestinationCity(City destinationCity) { this.destinationCity = destinationCity; }
    
    public Integer getDistanceInKm() { return distanceInKm; }
    public void setDistanceInKm(Integer distanceInKm) { this.distanceInKm = distanceInKm; }
}
