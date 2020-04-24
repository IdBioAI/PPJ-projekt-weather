package com.weather.weather.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityMgRepository extends MongoRepository<CityMg, Integer> {
}
