package com.travelease.services;

import com.travelease.models.City;
import com.travelease.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    
    @Autowired
    private CityRepository cityRepository;
    
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
    
    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }
    
    public List<City> searchCities(String keyword) {
        return cityRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public City saveCity(City city) {
        // Ensure state is not null before saving
        if (city.getState() == null) {
            city.setState("Unknown");
        }
        return cityRepository.save(city);
    }
    
    public boolean deleteCity(Long id) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Additional method to verify city existence
    public boolean cityExists(Long id) {
        return cityRepository.existsById(id);
    }
}