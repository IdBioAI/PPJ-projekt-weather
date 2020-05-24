package com.weather.weather.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityMgRepository extends MongoRepository<CityMg, Integer> {
    public List<CityMg> findByName(String name);
    public List<CityMg> findByNameAndDateGreaterThan(String name, long value);
    public void deleteByName(String name);
    public void deleteByDateAndName(long date, String name);
    public void deleteByDateLessThan(long date);
}
