package com.weather.weather;

import com.weather.model.CityMg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityMgRepository extends MongoRepository<CityMg, Integer> {
}
