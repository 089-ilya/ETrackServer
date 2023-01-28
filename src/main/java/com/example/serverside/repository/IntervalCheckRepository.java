package com.example.serverside.repository;

import com.example.serverside.entity.IntervalCheck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface IntervalCheckRepository extends MongoRepository<IntervalCheck, String> {

    Optional<IntervalCheck> findFirstByZoneIdOrderByTimeDesc(String zoneId);

    List<IntervalCheck> findAllByZoneIdAndAndTimeGreaterThan(String zoneId, Instant lastWeek);

}
