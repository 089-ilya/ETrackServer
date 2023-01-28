package com.example.serverside.service;

import com.example.serverside.entity.IntervalCheck;
import com.example.serverside.repository.IntervalCheckRepository;
import com.example.serverside.dto.IntervalCheckDTO;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntervalCheckService {

    private final IntervalCheckRepository intervalCheckRepository;
    private final MongoTemplate mongoTemplate;

    public void addCheck(IntervalCheckDTO intervalCheckDTO) {
        Optional<IntervalCheck> last = intervalCheckRepository.findFirstByZoneIdOrderByTimeDesc(intervalCheckDTO.getZoneId());

        if (!last.isPresent() || last.get().getIsOn() != intervalCheckDTO.getIsOn()) {
            intervalCheckRepository.save(intervalCheckDTO.toEntity());
        } else {
            last.get().setLastUpdatedTime(ZonedDateTime.now().toInstant());
            intervalCheckRepository.save(last.get());
        }
    }

    public void addAllChecks(List<IntervalCheckDTO> intervalCheckDTOList) {
        log.info("Add all checks {}", intervalCheckDTOList );
        divideToBatchesAndSave(intervalCheckDTOList);
    }

    private void divideToBatchesAndSave(List<IntervalCheckDTO> intervalCheckDTOList) {
        if (intervalCheckDTOList.size() == 0) {
            return;
        }
        IntervalCheckDTO first = intervalCheckDTOList.get(0);
        Optional<IntervalCheckDTO> firstReverseElOpt = intervalCheckDTOList.stream().filter(el -> el.getIsOn() != first.getIsOn()).findFirst();
        int endPeriodIndex;
        endPeriodIndex = firstReverseElOpt
                .map(intervalCheckDTO -> intervalCheckDTOList.indexOf(intervalCheckDTO) - 1)
                .orElseGet(() -> intervalCheckDTOList.size() - 1);
        IntervalCheckDTO last = intervalCheckDTOList.get(endPeriodIndex);
        IntervalCheck intervalCheck = first.toEntity();
        intervalCheck.setLastUpdatedTime(last.getTime());
        intervalCheckRepository.save(intervalCheck);
        List<IntervalCheckDTO> newCollection = intervalCheckDTOList.stream()
                .skip(endPeriodIndex + 1)
                .collect(Collectors.toList());
        divideToBatchesAndSave(newCollection);
    }

    public List<String> findUniqueZones() {
        List<String> zones = new ArrayList<>();
        DistinctIterable<String> distinctIterable = mongoTemplate
                .getCollection("intervalCheck").distinct("zoneId", String.class);
        MongoCursor cursor = distinctIterable.iterator();
        while (cursor.hasNext()) {
            String zone = (String)cursor.next();
            zones.add(zone);
        }
        return zones;
    }

    public List<IntervalCheck> findZoneInformation(String zoneId) {
        return intervalCheckRepository.findAllByZoneIdAndAndTimeGreaterThan(zoneId, ZonedDateTime.now().minusWeeks(1).toInstant());
    }

}
